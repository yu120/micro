package cn.micro.biz.pubsrv.pay.wx;

import cn.micro.biz.commons.auth.MicroAuthContext;
import cn.micro.biz.commons.exception.support.MicroBadRequestException;
import cn.micro.biz.commons.exception.support.MicroErrorException;
import cn.micro.biz.entity.orders.OrdersEntity;
import cn.micro.biz.entity.orders.OrdersGoodsEntity;
import cn.micro.biz.pubsrv.pay.PayChannelEnum;
import cn.micro.biz.type.orders.OrdersStatusEnum;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.github.wxpay.sdk.WXPay;
import com.github.wxpay.sdk.WXPayConstants;
import com.github.wxpay.sdk.WXPayUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.math.BigDecimal;
import java.net.URLEncoder;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.*;

@Slf4j
public class WXPayService {

    private static final WXPayConstants.SignType SIGN_TYPE = WXPayConstants.SignType.MD5;
    private static final String ACCESS_TOKEN_URL = "https://api.weixin.qq.com/sns/oauth2/access_token?" +
            "appid=%s&secret=%s&code=%s&grant_type=authorization_code";

    private WXPay wxpay;
    private WXPayProperties wxPayProperties;

    public static void main(String[] args) throws Exception {
        String url = String.format(WXPayService.ACCESS_TOKEN_URL, "wx70786ff0e385406a", "6753e1839fc95902f15841ddc78b6c5d", "061rERX30XKevJ1p91Y30h29Y30rERXY");
        Document doc = Jsoup.connect(url).get();
        System.out.println(doc.text());
    }

    public WXPayService(WXPayProperties wxPayProperties) {
        this.wxPayProperties = wxPayProperties;
        try {
            this.wxpay = new WXPay(wxPayProperties, false);
        } catch (Exception e) {
            throw new MicroErrorException(e.getMessage(), e);
        }
    }

    public Map<String, String> mWebPay(OrdersEntity order, String code, OrdersGoodsEntity orderGoods) throws Exception {
        // 创建统一下单数据
        Map<String, String> data = new HashMap<>();
        // 货币类型
        data.put(WxPayConstants.FEE_TYPE, WxPayConstants.FEE_TYPE_DEFAULT);
        // 商品描述:有长度限制
        data.put(WxPayConstants.BODY, (orderGoods.getGoodsName().length() < 20) ?
                orderGoods.getGoodsName() : (orderGoods.getGoodsName().substring(0, 20) + "..."));
        // 通知地址
        data.put(WxPayConstants.NOTIFY_URL, wxPayProperties.getNotifyUrl());
        // 商户订单号
        data.put(WxPayConstants.OUT_TRADE_NO, order.getOrderNo());
        // 终端IP
        data.put(WxPayConstants.SP_BILL_CREATE_IP, MicroAuthContext.getRequestIPAddress());
        // 商品ID
        data.put(WxPayConstants.PRODUCT_ID, String.valueOf(orderGoods.getGoodsId()));
        // 总金额
        data.put(WxPayConstants.TOTAL_FEE, changeY2F(
                order.getOrderAmountTotal().setScale(2, BigDecimal.ROUND_HALF_UP).toString()));
        if (PayChannelEnum.WECHAT.getStatus() == order.getPayChannel()) {
            Document doc = Jsoup.connect(String.format(WXPayService.ACCESS_TOKEN_URL,
                    wxPayProperties.getAppID(), wxPayProperties.getAppSecret(), code)).get();
            JSONObject jsonObject = JSON.parseObject(doc.text());
            String openId = jsonObject.getString("openid");
            if (StringUtils.isBlank(openId)) {
                log.warn("获取openid结果为：{}", doc.text());
                throw new MicroBadRequestException("不能获取openid");
            }

            data.put(WxPayConstants.TRADE_TYPE, WxPayConstants.TRADE_TYPE_JSAPI);
            data.put(WxPayConstants.OPEN_ID, openId);

            log.info("创建微信订单[{}]：{}", order.getOrderNo(), data);
            Map<String, String> resp = wxpay.unifiedOrder(data);
            log.info("创建微信订单[{}]结果：{}", order.getOrderNo(), resp);
            if (WXPayUtil.isSignatureValid(resp, wxPayProperties.getKey())) {
                String returnCode = resp.get(WxPayConstants.RETURN_CODE);
                if (!"SUCCESS".equals(returnCode)) {
                    throw new MicroErrorException("发起交易通信失败");
                }

                String resultCode = resp.get(WxPayConstants.RESULT_CODE);
                if (!"SUCCESS".equals(resultCode)) {
                    throw new MicroErrorException("创建微信订单失败");
                }

                JSONObject result = JSON.parseObject(JSON.toJSONString(resp));
                SortedMap<String, String> params = new TreeMap<>();
                // 顺序不能换
                params.put("signType", SIGN_TYPE.name());
                params.put("appId", wxPayProperties.getAppID());
                params.put("timeStamp", String.valueOf(System.currentTimeMillis() / 1000));
                params.put("nonceStr", result.getString("nonce_str"));
                params.put("package", "prepay_id=" + result.get("prepay_id"));
                params.put("sign", WXPayUtil.generateSignature(params, wxPayProperties.getKey()));
                return params;
            }
        } else if (PayChannelEnum.WECHAT_H5.getStatus() == order.getPayChannel()) {
            data.put(WxPayConstants.TRADE_TYPE, WxPayConstants.TRADE_TYPE_MWEB);
            log.info("订单[{}]发起支付：{}", order.getOrderNo(), data);

            Map<String, String> resp = wxpay.unifiedOrder(data);
            log.info("订单[{}]支付结果：{}", order.getOrderNo(), resp);
            if (WXPayUtil.isSignatureValid(resp, wxPayProperties.getKey())) {
                String returnCode = resp.get(WxPayConstants.RETURN_CODE);
                if (!"SUCCESS".equals(returnCode)) {
                    throw new MicroErrorException("发起交易通信失败");
                }

                String resultCode = resp.get(WxPayConstants.RESULT_CODE);
                if (!"SUCCESS".equals(resultCode)) {
                    throw new MicroErrorException("创建微信订单失败");
                }

                Map<String, String> result = new HashMap<>();
                result.put("body", resp.get(WxPayConstants.MWEB_URL) + "&redirect_url=" +
                        URLEncoder.encode(wxPayProperties.getRedirectUrl() + order.getId(), "UTF-8"));
                return result;
            }
        } else {
            throw new MicroBadRequestException("非法支付类型");
        }

        throw new MicroErrorException("创建订单失败");
    }

    /**
     * 查询订单
     *
     * @param outTradeNo
     * @return
     */
    public OrdersEntity tradeQuery(String outTradeNo) {
        Map<String, String> request = new HashMap<>();
        request.put("out_trade_no", outTradeNo);

        try {
            log.info("Trade query request: {}", request);
            Map<String, String> resp = wxpay.orderQuery(request);
            log.info("Trade query response: {}", resp);
            if (!"SUCCESS".equals(resp.get("return_code"))) {
                throw new MicroErrorException("发起交易通信失败");
            }
            if (!"SUCCESS".equals(resp.get("result_code"))) {
                throw new MicroErrorException("查询微信订单失败");
            }

            OrdersEntity orders = new OrdersEntity();
            orders.setOrderStatus(OrdersStatusEnum.TRADE_SUCCESS);
            orders.setOutTradeNo(resp.get("transaction_id"));

            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
            simpleDateFormat.setTimeZone(TimeZone.getTimeZone("GMT+8"));
            orders.setPayTime(new Timestamp(simpleDateFormat.parse(resp.get("time_end")).getTime()));

            return orders;
        } catch (Exception e) {
            throw new MicroErrorException("订单查询异常", e);
        }
    }

    public boolean check(Map<String, String> params) throws Exception {
        return wxpay.isPayResultNotifySignatureValid(params);
    }

    /**
     * 将分为单位的转换为元 （除100）
     *
     * @param amount
     * @return
     * @throws Exception
     */
    public static String changeF2Y(String amount) throws Exception {
        if (!amount.matches("\\-?[0-9]+")) {
            throw new MicroErrorException("金额格式有误");
        }

        return BigDecimal.valueOf(Long.valueOf(amount)).divide(new BigDecimal(100)).toString();
    }

    /**
     * 将元为单位的转换为分 替换小数点，支持以逗号区分的金额
     *
     * @param amount
     * @return
     */
    public static String changeY2F(String amount) {
        // 处理包含,￥或者$的金额
        String currency = amount.replaceAll("\\$|\\￥|\\,", "");
        int index = currency.indexOf(".");
        int length = currency.length();
        Long amLong;
        if (index == -1) {
            amLong = Long.valueOf(currency + "00");
        } else if (length - index >= 3) {
            amLong = Long.valueOf((currency.substring(0, index + 3)).replace(".", ""));
        } else if (length - index == 2) {
            amLong = Long.valueOf((currency.substring(0, index + 2)).replace(".", "") + 0);
        } else {
            amLong = Long.valueOf((currency.substring(0, index + 1)).replace(".", "") + "00");
        }

        return amLong.toString();
    }

}
