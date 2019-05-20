package cn.micro.biz.pubsrv.cache.support;

import com.alicp.jetcache.anno.CacheType;
import lombok.Data;
import lombok.ToString;

import java.io.Serializable;
import java.util.concurrent.TimeUnit;

@Data
@ToString
public class MicroCacheRuleProperties implements Serializable {

    private CacheType cacheType = CacheType.LOCAL;
    private TimeUnit timeUnit = TimeUnit.SECONDS;
    private TimeUnit remoteTimeUnit = TimeUnit.SECONDS;
    private boolean token;
    /**
     * Local cache limit number
     */
    private int limit = 200;
    /**
     * Local expire after write
     */
    private long localExpire = 2000;
    /**
     * Remote expire after write
     */
    private long remoteExpire = 2000;

}