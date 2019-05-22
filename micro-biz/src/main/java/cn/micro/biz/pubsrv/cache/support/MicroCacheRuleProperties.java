package cn.micro.biz.pubsrv.cache.support;

import com.alicp.jetcache.anno.CacheType;
import lombok.Data;
import lombok.ToString;

import java.io.Serializable;
import java.time.Duration;

@Data
@ToString
public class MicroCacheRuleProperties implements Serializable {

    /**
     * Cache type
     * <p>
     * {@link CacheType}
     */
    private CacheType cacheType = CacheType.LOCAL;
    /**
     * Token auth
     */
    private boolean token;
    /**
     * Local cache limit number
     */
    private int limit = 200;
    /**
     * Local expire after write
     */
    private Duration localExpire = Duration.ofSeconds(60L);
    /**
     * Remote expire after write
     */
    private Duration remoteExpire = Duration.ofSeconds(60L);

}