package cn.micro.biz.pubsrv.sms;

import lombok.Data;

import java.util.Date;

/**
 * 短信发送日志
 *
 * @author lry
 */
@Data
public class SmsLog {

    private String id;
    /**
     * 手机
     */
    private String mobile;
    /**
     * 发送时间
     */
    private Date sendTime;
    /**
     * 发送JSON参数: Map<String, Object>
     */
    private String params;
    /**
     * 发送来源（一般是系统名称）
     */
    private String source;

    /**
     * IP
     */
    private String ip;
    /**
     * 短信发送响应请求ID
     */
    private String traceId;
    /**
     * 短信发送响应业务ID
     */
    private String bizId;
    /**
     * 短信发送响应code
     */
    private String code;
    /**
     * 短信发送响应消息
     */
    private String message;
    /**
     * 短信模板
     */
    private String templateCode;

}
