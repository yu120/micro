package cn.micro.biz.pubsrv.pay.ali;

import cn.micro.biz.commons.exception.support.MicroErrorException;
import cn.micro.biz.entity.order.Order;
import cn.micro.biz.entity.order.OrderGoods;
import cn.micro.biz.type.order.OrderStatusEnum;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.NameFilter;
import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.domain.AlipayTradeQueryModel;
import com.alipay.api.domain.AlipayTradeWapPayModel;
import com.alipay.api.internal.util.AlipaySignature;
import com.alipay.api.request.AlipayTradeQueryRequest;
import com.alipay.api.request.AlipayTradeWapPayRequest;
import com.alipay.api.response.AlipayTradeQueryResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.CharUtils;
import org.apache.commons.lang3.StringUtils;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Map;

@Slf4j
public class AliPayService {

    private static final String FORMAT = "json";
    private static final String CHARSET = "UTF-8";
    private static final String SIGN_TYPE = "RSA2";
    private static final String SERVER_URL = "https://openapi.alipay.com/gateway.do";
    private AlipayClient alipayClient;
    private AliPayProperties aliPayProperties;

    public AliPayService(AliPayProperties aliPayProperties) {
        this.aliPayProperties = aliPayProperties;
        this.alipayClient = new DefaultAlipayClient(SERVER_URL,
                aliPayProperties.getAppId(),
                aliPayProperties.getPrivateKey(),
                FORMAT, CHARSET,
                aliPayProperties.getAliPayPublicKey(),
                SIGN_TYPE);
    }

    /**
     * 支付宝支付
     *
     * @param order      {@link Order}
     * @param orderGoods {@link OrderGoods}
     * @return {@link Map<String, String>}
     */
    public Map<String, String> pay(Order order, OrderGoods orderGoods) {
        AlipayTradeWapPayModel model = new AlipayTradeWapPayModel();
        model.setOutTradeNo(order.getOrderNo());
        model.setTotalAmount(order.getOrderAmountTotal().setScale(2, BigDecimal.ROUND_HALF_UP).toString());
        model.setSubject(orderGoods.getGoodsName());
        model.setProductCode("QUICK_WAP_PAY");

        AlipayTradeWapPayRequest alipayRequest = new AlipayTradeWapPayRequest();
        alipayRequest.setNotifyUrl(aliPayProperties.getNotifyUrl());
        alipayRequest.setBizContent(this.toUnderLineJSONString(model));
        alipayRequest.setReturnUrl(aliPayProperties.getReturnUrl() + order.getId());

        try {
            log.info("创建支付宝支付订单: {}", JSON.toJSONString(alipayRequest));
            Map<String, String> result = new HashMap<>();
            result.put("body", alipayClient.pageExecute(alipayRequest).getBody());
            return result;
        } catch (AlipayApiException e) {
            throw new MicroErrorException("支付异常", e);
        }
    }

    /**
     * 支付宝验证签名
     *
     * @param params {@link Map<String, String>}
     * @return success true
     * @throws AlipayApiException Ali order Api Exception
     */
    public boolean check(Map<String, String> params) throws AlipayApiException {
        return AlipaySignature.rsaCheckV1(params, aliPayProperties.getAliPayPublicKey(), CHARSET, SIGN_TYPE);
    }

    /**
     * 支付宝订单查询
     *
     * @param outTradeNo out trade no
     * @return {@link Order}
     */
    public Order tradeQuery(String outTradeNo) {
        try {
            AlipayTradeQueryModel model = new AlipayTradeQueryModel();
            model.setOutTradeNo(outTradeNo);

            AlipayTradeQueryRequest request = new AlipayTradeQueryRequest();
            request.setBizContent(this.toUnderLineJSONString(model));
            AlipayTradeQueryResponse response = alipayClient.execute(request);
            if (response.isSuccess()) {
                if ("TRADE_SUCCESS".equals(response.getTradeStatus())
                        || "TRADE_FINISHED".equals(response.getTradeStatus())
                        || "TRADE_CLOSED".equals(response.getTradeStatus())) {
                    Order orders = new Order();
                    orders.setOutTradeNo(response.getTradeNo());
                    orders.setPayTime(new Timestamp(response.getSendPayDate().getTime()));
                    if ("TRADE_SUCCESS".equals(response.getTradeStatus())) {
                        orders.setOrderStatus(OrderStatusEnum.TRADE_SUCCESS);
                    } else if ("TRADE_FINISHED".equals(response.getTradeStatus())) {
                        orders.setOrderStatus(OrderStatusEnum.TRADE_FINISHED);
                    } else if ("TRADE_CLOSED".equals(response.getTradeStatus())) {
                        orders.setOrderStatus(OrderStatusEnum.TRADE_CLOSED);
                    }

                    return orders;
                }
            }
        } catch (Exception e) {
            log.error("支付宝订单[outTradeNo=" + outTradeNo + "]查询异常", e);
        }

        return null;
    }

    private String toUnderLineJSONString(Object object) {
        return JSON.toJSONString(object, (NameFilter) (obj, name, value) -> {
            if (null == name) {
                return "";
            }

            StringBuilder sbl = new StringBuilder(name);
            sbl.setCharAt(0, Character.toLowerCase(sbl.charAt(0)));
            name = sbl.toString();
            char[] chars = name.toCharArray();
            StringBuilder sb = new StringBuilder();
            for (char c : chars) {
                if (CharUtils.isAsciiAlphaUpper(c)) {
                    sb.append("_").append(StringUtils.lowerCase(CharUtils.toString(c)));
                } else {
                    sb.append(c);
                }
            }

            return sb.toString();
        });
    }

}
