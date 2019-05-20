package cn.micro.biz.pubsrv.redis;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@ConfigurationProperties(prefix = "micro.redis")
public class RedisProperties extends GenericObjectPoolConfig {

    private boolean enable;
    private String uri;
    private long timeout;

}
