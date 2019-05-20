package cn.micro.biz.pubsrv.email;

import lombok.Data;
import lombok.ToString;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.io.Serializable;

@Data
@ToString
@ConfigurationProperties(prefix = "micro.email")
public class EmailProperties implements Serializable {

    /**
     * email enable switch
     */
    private boolean enable;
    /**
     * SMTP server host. For instance, `smtp.example.com`.
     */
    private String host;
    /**
     * SMTP server port.
     */
    private Integer port;
    /**
     * 发件人昵称
     */
    private String personal;
    /**
     * Login user of the SMTP server.
     */
    private String username;
    /**
     * Login password of the SMTP server.
     */
    private String password;

}
