package cn.micro.biz.pubsrv.webhook;

import lombok.Data;
import lombok.ToString;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.io.Serializable;

/**
 * Micro Web Hook Properties
 *
 * @author lry
 */
@Data
@ToString
@ConfigurationProperties(prefix = "micro.webhook")
public class MicroWebHookProperties implements Serializable {

    /**
     * Ding Talk access_token
     */
    private String dingTalkAccessToken;
    /**
     * Beary Chat access_token
     */
    private String bearyChatAccessToken;

}
