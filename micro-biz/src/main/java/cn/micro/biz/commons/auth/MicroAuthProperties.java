package cn.micro.biz.commons.auth;

import lombok.Data;
import lombok.ToString;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.io.Serializable;
import java.time.Duration;

/**
 * Micro Properties
 *
 * @author lry
 */
@Data
@ToString
@ConfigurationProperties(prefix = "micro.auth")
public class MicroAuthProperties implements Serializable {

    public static final String TIMESTAMP_KEY = "timestamp";
    public static final String SIGN_KEY = "sign";

    /**
     * 访问Token过期时间
     */
    private Duration tokenExpires = Duration.ofSeconds(7200L);
    /**
     * 刷新Token过期时间
     */
    private Duration refreshToken = Duration.ofSeconds(86400L);
    /**
     * 是否自动刷新鉴权资源配置
     */
    private boolean autoAuthRefresh = true;
    /**
     * 授权资源配置刷新周期时间
     */
    private Duration authRefresh = Duration.ofSeconds(60L);

    /**
     * 使用Redis校验请求过期
     */
    private boolean requestExpire = false;
    /**
     * 使用Redis校验Token过期
     */
    private boolean tokenExpire = false;

    /**
     * 校验Token开关
     */
    private boolean checkToken = true;
    /**
     * 校验客户端时间戳
     */
    private boolean checkTime = true;
    /**
     * 校验请求签名开关
     */
    private boolean checkSign = true;

    /**
     * 容错时间差
     */
    private Duration faultTolerant = Duration.ofSeconds(60L);

}
