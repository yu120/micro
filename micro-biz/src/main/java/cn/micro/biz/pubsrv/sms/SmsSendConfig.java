package cn.micro.biz.pubsrv.sms;

import lombok.Data;

/**
 * 短信配置
 *
 * @author lry
 */
@Data
public class SmsSendConfig {

    /**
     * 应用代码
     */
    private String app;
    /**
     * 业务代码
     */
    private String biz;

    /**
     * 短信服务商代码
     */
    private String store;
    /**
     * 短信签名
     */
    private String signName;
    /**
     * 短信模板CODE
     */
    private String templateCode;
    /**
     * 短信发送消息内容模板
     */
    private String templateMessage;

    /**
     * true表示需要同步发送
     */
    private boolean syncSend = false;
    /**
     * true表示为调试模式，不会真正发送短信(测试时用)
     */
    private boolean debug = false;

    /**
     * true表示需要发送至钉钉消息群(测试时用)
     */
    private boolean sendDingTalk = false;
    /**
     * 钉钉群名称
     */
    private String dingTalkName;
    /**
     * 钉钉群号
     */
    private String dingTalkNo;
    /**
     * 钉钉群Token
     */
    private String dingTalkToken;

}