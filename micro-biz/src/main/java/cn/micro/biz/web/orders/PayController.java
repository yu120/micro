package cn.micro.biz.web.orders;

import cn.micro.biz.commons.auth.PreAuth;
import cn.micro.biz.pubsrv.pay.*;
import cn.micro.biz.pubsrv.pay.PayChannelEnum;
import cn.micro.biz.entity.orders.OrdersEntity;
import cn.micro.biz.pubsrv.pay.wx.WxPayConstants;
import cn.micro.biz.pubsrv.pay.wx.WXPayService;
import cn.micro.biz.type.orders.OrdersStatusEnum;
import com.alibaba.fastjson.JSON;
import com.github.wxpay.sdk.WXPayUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.TimeZone;

/**
 * Pay Controller
 *
 * @author lry
 */
@Slf4j
@PreAuth
@Validated
@RestController
@RequestMapping("pay")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class PayController {

    private final PayService payService;
    private final PayOrdersService payOrdersService;
    private final PayProperties payProperties;

    @RequestMapping(value = "ali-pay-notify", method = RequestMethod.POST)
    public void aliPayNotify(HttpServletRequest request, HttpServletResponse response) throws Exception {
        // 获取支付宝POST过来反馈信息
        Map<String, String> params = this.getParameters(request);
        log.info("[支付宝支付异步通知数据]: {}", params);

        // 计算得出通知验证结果
        if (payService.check(PayChannelEnum.ALIPAY, params)) {
            // 获取支付宝的通知返回参数，可参考技术文档中页面跳转同步通知参数列表(以下仅供参考)
            String orderNo = params.get(WxPayConstants.OUT_TRADE_NO);
            String tradeNo = params.get(WxPayConstants.TRADE_NO);
            String tradeStatus = params.get(WxPayConstants.TRADE_STATUS);
            if (StringUtils.isBlank(orderNo) || StringUtils.isBlank(tradeNo) || StringUtils.isBlank(tradeStatus)) {
                log.warn("非法请求参数: orderNo={}, tradeNo={}, tradeStatus={}", orderNo, tradeNo, tradeStatus);
                this.responseAliPayNotify(false, response);
                return;
            }

            String sellerId = params.get(WxPayConstants.SELLER_ID);
            String totalAmount = params.get(WxPayConstants.TOTAL_AMOUNT);
            if (totalAmount == null || !payProperties.getAliPay().getSellerId().equals(sellerId)) {
                log.warn("非法订单: orderNo={}, sellerId={}, totalAmount={}", orderNo, sellerId, totalAmount);
                this.responseAliPayNotify(false, response);
                return;
            }

            OrdersEntity orders = payOrdersService.selectPayOrderByOrderNo(orderNo);
            if (orders == null) {
                log.warn("非法订单-订单不存在: orderNo={}", orderNo);
                this.responseAliPayNotify(false, response);
                return;
            }
            double totalMoney = orders.getOrderAmountTotal().doubleValue();
            if (totalMoney != Double.valueOf(totalAmount)) {
                log.warn("非法订单-支付金额不匹配: orderNo={}, totalMoney={}, totalAmount={}", orderNo, totalMoney, totalAmount);
                this.responseAliPayNotify(false, response);
                return;
            }

            // 防止重复更新数据
            if (orders.getOrderStatus() != null
                    && OrdersStatusEnum.WAIT_SEND_PAY != orders.getOrderStatus()
                    && OrdersStatusEnum.WAIT_BUYER_PAY != orders.getOrderStatus()) {
                this.responseAliPayNotify(true, response);
                return;
            }

            String gmtPayment = params.get(WxPayConstants.GMT_PAYMENT);
            String buyerId = params.get(WxPayConstants.BUYER_ID);
            String buyerLogonId = params.get(WxPayConstants.BUYER_LOGON_ID);

            // 更新订单信息
            OrdersEntity updateOrders = new OrdersEntity();
            updateOrders.setId(orders.getId());
            updateOrders.setOutTradeNo(tradeNo);

            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            simpleDateFormat.setTimeZone(TimeZone.getTimeZone("GMT+8"));

            try {
                updateOrders.setPayTime(new Timestamp(simpleDateFormat.parse(gmtPayment).getTime()));
            } catch (Exception e) {
                updateOrders.setPayTime(new Timestamp(System.currentTimeMillis()));
            }
            if (payOrdersService.updatePayOrderById(updateOrders)) {
                log.info("订单更新成功：orderNo={}, data={}", orderNo, updateOrders);
            } else {
                log.warn("订单更新失败：orderNo={}, data={}", orderNo, updateOrders);
            }

            this.responseAliPayNotify(true, response);
        } else {
            this.responseAliPayNotify(false, response);
        }
    }

    @RequestMapping(value = "wx-pay-notify", method = RequestMethod.POST)
    public void wxPayNotify(@RequestBody String notifyData, HttpServletResponse response) throws Exception {
        // 获取微信发过来的反馈信息
        Map<String, String> params = WXPayUtil.xmlToMap(notifyData);
        log.info("[微信支付异步通知数据]: {}", params);

        if (payService.check(PayChannelEnum.WECHAT, params)) {
            // 验证成功
            String orderNo = params.get(WxPayConstants.OUT_TRADE_NO);
            String tradeNo = params.get(WxPayConstants.TRANSACTION_ID);
            String timeEnd = params.get(WxPayConstants.TIME_END);
            String resultCode = params.get(WxPayConstants.RESULT_CODE);
            String deviceInfo = params.get(WxPayConstants.DEVICE_INFO);
            String openId = params.get(WxPayConstants.OPENID);
            // 金额
            String totalFee = params.get(WxPayConstants.TOTAL_FEE);
            if (StringUtils.isBlank(totalFee)) {
                log.warn("非法订单-非法金额: totalFee={}", totalFee);
                this.responseWxPayNotify(false, response);
                return;
            }
            totalFee = WXPayService.changeF2Y(totalFee);

            OrdersEntity orders = payOrdersService.selectPayOrderByOrderNo(orderNo);
            if (orders == null) {
                log.warn("非法订单-订单不存在: orderNo={}", orderNo);
                this.responseWxPayNotify(false, response);
                return;
            }

            double totalMoney = orders.getOrderAmountTotal().doubleValue();
            if (totalMoney != Double.valueOf(totalFee)) {
                log.warn("非法订单-支付金额不匹配: orderNo={}, totalMoney={}, totalFee={}", orderNo, totalMoney, totalFee);
                this.responseWxPayNotify(false, response);
                return;
            }

            // 防止重复更新数据
            if (orders.getOrderStatus() != null
                    && OrdersStatusEnum.WAIT_SEND_PAY != orders.getOrderStatus()
                    && OrdersStatusEnum.WAIT_BUYER_PAY != orders.getOrderStatus()) {
                this.responseWxPayNotify(false, response);
                return;
            }
            // 如果订单已经成功了,则不能重复在操作
            if (OrdersStatusEnum.TRADE_SUCCESS == orders.getOrderStatus()) {
                this.responseWxPayNotify(true, response);
                return;
            }

            // 更新订单信息
            OrdersEntity updateOrders = new OrdersEntity();
            updateOrders.setId(orders.getId());
            updateOrders.setOutTradeNo(tradeNo);
            updateOrders.setOrderStatus(OrdersStatusEnum.TRADE_SUCCESS);

            try {
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
                simpleDateFormat.setTimeZone(TimeZone.getTimeZone("GMT+8"));
                updateOrders.setPayTime(new Timestamp(simpleDateFormat.parse(timeEnd).getTime()));
            } catch (Exception e) {
                updateOrders.setPayTime(new Timestamp(System.currentTimeMillis()));
            }

            log.info("即将更新订单：orderNo={}, data={}", orderNo, JSON.toJSONString(updateOrders));
            if (payOrdersService.updatePayOrderById(updateOrders)) {
                log.info("订单更新成功：orderNo={}", orderNo);
            } else {
                log.warn("订单更新失败：orderNo={}", orderNo);
            }

            this.responseWxPayNotify(true, response);
        } else {
            this.responseWxPayNotify(false, response);
        }
    }


    // =============== Internal Method


    private Map<String, String> getParameters(HttpServletRequest request) {
        Map<String, String> params = new HashMap<>();
        Map requestParams = request.getParameterMap();
        for (Object o : requestParams.keySet()) {
            String name = (String) o;
            String[] values = (String[]) requestParams.get(name);
            String valueStr = "";
            for (int i = 0; i < values.length; i++) {
                valueStr = (i == values.length - 1) ? valueStr + values[i] : valueStr + values[i] + ",";
            }
            params.put(name, valueStr);
        }

        return params;
    }

    private void responseAliPayNotify(boolean flag, HttpServletResponse response) throws IOException {
        if (flag) {
            response.getWriter().println("success");
        } else {
            response.getWriter().println("fail");
        }
        response.getWriter().flush();
        response.getWriter().close();
    }

    private void responseWxPayNotify(boolean flag, HttpServletResponse response) throws Exception {
        Map<String, String> returnMap = new HashMap<>();
        if (flag) {
            returnMap.put("return_code", "SUCCESS");
            returnMap.put("return_msg", "OK");
        } else {
            returnMap.put("return_code", "FAIL");
        }
        response.getWriter().println(WXPayUtil.mapToXml(returnMap));
        response.getWriter().flush();
        response.getWriter().close();
    }

}
