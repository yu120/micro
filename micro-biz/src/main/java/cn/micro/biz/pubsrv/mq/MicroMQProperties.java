package cn.micro.biz.pubsrv.mq;

import lombok.Data;
import lombok.ToString;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.io.Serializable;

/**
 * MQ Properties
 *
 * @author lry
 */
@Data
@ToString
@ConfigurationProperties(prefix = "micro.mq")
public class MicroMQProperties implements Serializable {

    /**
     * MQ enable switch
     */
    private boolean enable;
    /**
     * Connection uri of the MQ server.
     */
    private String uri;
    /**
     * Connection user of the MQ server.
     */
    private String username;
    /**
     * Connection password of the MQ server.
     */
    private String password;

}
