package cn.micro.biz.pubsrv.wechat;

import lombok.Data;
import lombok.ToString;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.io.Serializable;

/**
 * Micro Wx Properties
 *
 * @author lry
 */
@Data
@ToString
@ConfigurationProperties(prefix = "micro.wx")
public class MicroWxProperties implements Serializable {

    private String appId;
    private String secret;

}
