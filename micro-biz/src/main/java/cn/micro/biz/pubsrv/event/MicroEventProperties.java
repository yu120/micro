package cn.micro.biz.pubsrv.event;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Micro Event Properties
 *
 * @author lry
 */
@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@ConfigurationProperties(prefix = MicroEventProperties.PREFIX)
public class MicroEventProperties extends GenericObjectPoolConfig {

    public static final String PREFIX = "micro.event";

    /**
     * Event enable
     */
    private boolean enable;

    /**
     * Core thread number
     */
    private int coreThread = 5;
    /**
     * Max thread number
     */
    private int maxThread = 30;
    /**
     * Queue size
     */
    private int queueSize = 200;


}
