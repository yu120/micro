package cn.micro.biz.pubsrv.cache.support;

import cn.micro.biz.pubsrv.cache.CacheKey;
import cn.micro.biz.pubsrv.cache.serializer.CacheSerializer;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.time.Duration;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * Micro Cache Properties
 *
 * @author lry
 */
@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@ConfigurationProperties(prefix = MicroCacheProperties.PREFIX)
public class MicroCacheProperties extends GenericObjectPoolConfig {

    public static final String PREFIX = "micro.cache";

    /**
     * Cache enable
     */
    private boolean enable;
    /**
     * Cache key prefix
     */
    private String keyPrefix = "MICRO:CACHE";

    // ==== local config

    /**
     * Micro cache local type
     */
    private MicroCacheType localType = MicroCacheType.LINKED_HASH_MAP;

    // ==== remote config

    /**
     * Micro cache remote type
     */
    private MicroCacheType remoteType = MicroCacheType.REDIS_LETTUCE;
    /**
     * Remote value serializer
     */
    private CacheSerializer remoteSerializer = CacheSerializer.FASTJSON;
    /**
     * Remote connection uri
     */
    private String uri;
    /**
     * Remote connection protocol
     */
    private String protocol;
    /**
     * Remote connection host
     */
    private String host;
    /**
     * Remote connection port
     */
    private int port = 6379;
    /**
     * Remote connection password
     */
    private String password;
    /**
     * Remote connection timeout
     */
    private Duration timeout = Duration.ofSeconds(60L);

    // ===== Async reload cache config

    /**
     * Async reload core number
     */
    private int reloadCore = 5;
    /**
     * Async reload max number
     */
    private int reloadMax = 30;
    /**
     * Async reload queue number
     */
    private int reloadQueue = 200;

    // ===== Global cache rule config

    /**
     * Global Local limit
     */
    private int localLimit = 200;
    /**
     * Global local expire after write
     */
    private Duration localExpire = Duration.ofSeconds(60L);
    /**
     * Global remote expire after write
     */
    private Duration remoteExpire = Duration.ofSeconds(60L);

    /**
     * Cache rule config list
     */
    private Map<CacheKey, MicroCacheRuleProperties> cacheRules = new LinkedHashMap<>();

    public String getUri() {
        if (uri == null || uri.length() == 0) {
            return protocol + "://" + host + ":" + port;
        }

        return uri;
    }

}
