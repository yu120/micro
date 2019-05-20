package cn.micro.biz.pubsrv.pay.ali;

import lombok.Data;
import lombok.ToString;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.io.Serializable;

@Data
@ToString
@ConfigurationProperties(prefix = "micro.pay.ali-pay")
public class AliPayProperties implements Serializable {

    /**
     * 商户支付宝账户(一般为邮箱或者手机号)
     */
    private String sellerId;
    /**
     * 支付宝分配给开发者的应用ID
     */
    private String appId;
    /**
     * 同步通知跳转接口
     */
    private String returnUrl;
    /**
     * 异步支付结果通知接口
     */
    private String notifyUrl;
    /**
     * 商户私钥
     */
    private String privateKey;
    /**
     * 支付宝公钥(非商户公钥)
     */
    private String aliPayPublicKey;

}
