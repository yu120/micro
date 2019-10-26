package cn.micro.biz.pubsrv.sms;

import lombok.Data;

import java.io.Serializable;
import java.util.Map;

/**
 * 短信发送参数
 *
 * @author lry
 */
@Data
public class SmsSendParam implements Serializable {

    /**
     * 应用代码
     */
    private String app;
    /**
     * 业务代码
     */
    private String biz;

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
     * 请求参数KEY-VALUE
     */
    private Map<String, Object> params;

}
