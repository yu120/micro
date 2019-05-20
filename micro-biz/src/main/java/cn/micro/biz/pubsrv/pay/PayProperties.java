package cn.micro.biz.pubsrv.pay;

import cn.micro.biz.pubsrv.pay.ali.AliPayProperties;
import cn.micro.biz.pubsrv.pay.wx.WXPayProperties;
import lombok.Data;
import lombok.ToString;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.io.Serializable;

@Data
@ToString
@ConfigurationProperties(prefix = "micro.pay")
public class PayProperties implements Serializable {

    private boolean enable = false;
    /**
     * 支付宝支付配置
     */
    private AliPayProperties aliPay;

    /**
     * 微信支付配置
     */
    private WXPayProperties wxPay;

}
