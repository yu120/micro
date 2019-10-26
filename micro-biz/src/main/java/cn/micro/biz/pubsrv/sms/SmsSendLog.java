package cn.micro.biz.pubsrv.sms;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 短信发送日志
 *
 * @author lry
 */
@Data
public class SmsSendLog implements Serializable {


    // === 时间信息

    /**
     * 准备时间(系统收到消息时间)
     */
    private Date prepareTime;
    /**
     * 发送时间(系统发出消息时间)
     */
    private Date sendTime;


    // === 配置信息

    /**
     * 应用代码
     */
    private String app;
    /**
     * 业务代码
     */
    private String biz;
    /**
     * 短信类别: 验证码、通知
     */
    private String category;

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
    private String message;

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


    // === 请求参数

    /**
     * IP
     */
    private String ip;
    /**
     * 请求追踪ID
     */
    private String traceId;
    /**
     * 手机号
     */
    private String mobile;
    /**
     * 请求参数KEY-VALUE JSON
     */
    private String paramsJson;
    /**
     * 发送结果状态,true表示成功
     */
    private boolean success = false;
    /**
     * 结果JSON参数
     */
    private String resultJson;

}
