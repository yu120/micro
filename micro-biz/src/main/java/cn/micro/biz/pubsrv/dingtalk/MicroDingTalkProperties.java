package cn.micro.biz.pubsrv.dingtalk;

import lombok.Data;
import lombok.ToString;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.io.Serializable;

/**
 * Micro Ding Talk Properties
 *
 * @author lry
 */
@Data
@ToString
@ConfigurationProperties(prefix = "micro.ding-talk")
public class MicroDingTalkProperties implements Serializable {

    private boolean enable;
    private String accessToken;

}
