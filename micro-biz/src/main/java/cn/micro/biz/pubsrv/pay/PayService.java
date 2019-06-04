package cn.micro.biz.pubsrv.pay;

import cn.micro.biz.commons.exception.support.MicroBadRequestException;
import cn.micro.biz.entity.order.OrderGoods;
import cn.micro.biz.pubsrv.pay.ali.AliPayService;
import cn.micro.biz.entity.order.Order;
import cn.micro.biz.pubsrv.pay.wx.WXPayService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * 支付服务
 *
 * @author lry
 */
@Slf4j
@Service
@EnableConfigurationProperties(PayProperties.class)
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class PayService implements InitializingBean {

    private final PayProperties payProperties;

    private AliPayService alipayService;
    private WXPayService wxPayService;

    @Override
    public void afterPropertiesSet() {
        if (!payProperties.isEnable()) {
            return;
        }

        this.alipayService = new AliPayService(payProperties.getAliPay());
        this.wxPayService = new WXPayService(payProperties.getWxPay());
    }

    public Map<String, String> pay(Order order, String code, OrderGoods orderGoods) throws Exception {
        if (PayChannelEnum.ALIPAY.getStatus() == order.getPayChannel()) {
            return alipayService.pay(order, orderGoods);
        } else if (PayChannelEnum.WECHAT.getStatus() == order.getPayChannel()
                || PayChannelEnum.WECHAT_H5.getStatus() == order.getPayChannel()) {
            return wxPayService.mWebPay(order, code, orderGoods);
        } else {
            throw new MicroBadRequestException("非法支付方式");
        }
    }

    public boolean check(PayChannelEnum payBy, Map<String, String> params) throws Exception {
        if (PayChannelEnum.ALIPAY.getStatus() == payBy.getStatus()) {
            return alipayService.check(params);
        } else if (PayChannelEnum.WECHAT.getStatus() == payBy.getStatus()) {
            return wxPayService.check(params);
        } else {
            throw new MicroBadRequestException("非法支付方式");
        }
    }

    public Order tradeQuery(PayChannelEnum payBy, String outTradeNo) {
        if (PayChannelEnum.ALIPAY.getStatus() == payBy.getStatus()) {
            return alipayService.tradeQuery(outTradeNo);
        } else if (PayChannelEnum.WECHAT.getStatus() == payBy.getStatus()) {
            return wxPayService.tradeQuery(outTradeNo);
        } else {
            throw new MicroBadRequestException("非法支付方式");
        }
    }

}
