package cn.micro.biz.pubsrv.sms;

import lombok.Data;
import lombok.ToString;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.io.Serializable;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Sms Properties
 *
 * @author lry
 */
@Data
@ToString
@ConfigurationProperties(prefix = "micro.sms.ali-yun")
public class AliYunSmsProperties implements Serializable {

    private boolean enable;

    private long connectionTimeoutMillis = 15000L;
    private long readTimeoutMillis = 15000L;
    private long writeTimeoutMillis = 15000L;

    private String accessKey;
    private String secretKey;

    /**
     * Map<TemplateCode, {SignName,templateParams}>
     */
    private Map<String, SmsTemplatesProperties> templates = new LinkedHashMap<>();

}
