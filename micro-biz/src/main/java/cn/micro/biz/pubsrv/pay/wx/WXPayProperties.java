package cn.micro.biz.pubsrv.pay.wx;

import com.github.wxpay.sdk.IWXPayDomain;
import com.github.wxpay.sdk.WXPayConfig;
import com.github.wxpay.sdk.WXPayConstants;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.Serializable;

@Data
@Slf4j
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@ConfigurationProperties(prefix = "micro.pay.wx-pay")
public class WXPayProperties extends WXPayConfig implements Serializable {

    /**
     * 微信支付商户号
     */
    private String mchID;
    /**
     * APP ID
     */
    private String appID;
    /**
     * API 秘钥
     */
    private String key;
    /**
     * App Secret
     */
    private String appSecret;
    /**
     * 同步通知跳转接口
     */
    private String redirectUrl;
    /**
     * 异步支付结果通知接口
     * <p>
     * 通知url必须为外网可访问的url，不能携带参数
     */
    private String notifyUrl;
    /**
     * HTTP(S) 连接超时时间，单位毫秒
     */
    private int httpConnectTimeoutMs = 8000;
    /**
     * HTTP(S) 读数据超时时间，单位毫秒
     */
    private int httpReadTimeoutMs = 10000;
    /**
     * 是否自动上报
     * <p>
     * true表示自动上报
     */
    private boolean shouldAutoReport = true;
    /**
     * 进行健康上报的线程的数量
     */
    private int reportWorkerNum = 6;
    /**
     * 健康上报缓存消息的最大数量。会有线程去独立上报
     * 粗略计算：加入一条消息200B，10000消息占用空间 2000 KB，约为2MB，可以接受
     */
    private int reportQueueMaxSize = 10000;
    /**
     * 批量上报，一次最多上报多个数据
     */
    private int reportBatchSize = 10;
    /**
     * 用于多域名容灾自动切换
     */
    private WXPayDomain wxPayDomain = new WXPayDomain();
    /**
     * 签名方式
     */
    private WXPayConstants.SignType signType = WXPayConstants.SignType.MD5;

    /**
     * 获取商户证书内容
     *
     * @return {@link InputStream}
     */
    @Override
    public InputStream getCertStream() {
        return new ByteArrayInputStream(this.getAppSecret().getBytes());
    }

    @Override
    public IWXPayDomain getWXPayDomain() {
        return wxPayDomain;
    }

    @Override
    public boolean shouldAutoReport() {
        return shouldAutoReport;
    }

}
