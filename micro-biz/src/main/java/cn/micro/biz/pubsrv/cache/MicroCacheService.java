package cn.micro.biz.pubsrv.cache;

import cn.micro.biz.commons.auth.MicroAuthContext;
import cn.micro.biz.commons.exception.support.MicroErrorException;
import cn.micro.biz.pubsrv.cache.support.MicroCacheProperties;
import cn.micro.biz.pubsrv.cache.support.MicroCacheRuleProperties;
import cn.micro.biz.pubsrv.cache.serializer.FastjsonValueDecoder;
import cn.micro.biz.pubsrv.cache.serializer.FastjsonValueEncoder;
import cn.micro.biz.pubsrv.cache.support.MicroCacheType;
import com.alicp.jetcache.Cache;
import com.alicp.jetcache.MultiLevelCacheBuilder;
import com.alicp.jetcache.embedded.LinkedHashMapCacheBuilder;
import com.alicp.jetcache.redis.lettuce4.RedisLettuceCacheBuilder;
import com.alicp.jetcache.support.*;
import com.lambdaworks.redis.RedisClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Function;

/**
 * Micro Cache Configuration
 *
 * @author lry
 */
@Slf4j
@Configuration
@EnableConfigurationProperties(MicroCacheProperties.class)
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@ConditionalOnProperty(prefix = MicroCacheProperties.PREFIX, name = "enable", havingValue = "true")
public class MicroCacheService implements InitializingBean, DisposableBean {

    private static ThreadPoolExecutor threadPoolExecutor;
    private static ConcurrentMap<CacheKey, Cache<Object, Object>> cacheMap = new ConcurrentHashMap<>();
    private static ConcurrentMap<CacheKey, MicroCacheRuleProperties> cacheRuleMap = new ConcurrentHashMap<>();

    private final MicroCacheProperties properties;

    private RedisClient redisClient;
    private Function<Object, byte[]> valueEncoder;
    private Function<byte[], Object> valueDecoder;

    @Override
    public void afterPropertiesSet() throws Exception {
        switch (properties.getRemoteSerializer()) {
            case JAVA:
                valueEncoder = JavaValueEncoder.INSTANCE;
                valueDecoder = JavaValueDecoder.INSTANCE;
                break;
            case KRYO:
                valueEncoder = KryoValueEncoder.INSTANCE;
                valueDecoder = KryoValueDecoder.INSTANCE;
                break;
            case FASTJSON:
                valueEncoder = FastjsonValueEncoder.INSTANCE;
                valueDecoder = FastjsonValueDecoder.INSTANCE;
                break;
            default:
        }

        GenericObjectPoolConfig poolConfig = new GenericObjectPoolConfig();
        poolConfig.setMinIdle(properties.getMinIdle());
        poolConfig.setMaxIdle(properties.getMaxIdle());
        poolConfig.setMaxTotal(properties.getMaxTotal());
        this.redisClient = RedisClient.create(properties.getUri());
        redisClient.setDefaultTimeout(properties.getTimeout().getSeconds(), TimeUnit.SECONDS);
        if (!properties.getCacheRules().isEmpty()) {
            for (Map.Entry<CacheKey, MicroCacheRuleProperties> entry : properties.getCacheRules().entrySet()) {
                cacheMap.put(entry.getKey(), this.buildCache(entry.getValue()));
                cacheRuleMap.put(entry.getKey(), entry.getValue());
            }
        }

        // 队列满了丢任务不异常
        threadPoolExecutor = new ThreadPoolExecutor(
                properties.getReloadCore(),
                properties.getReloadMax(),
                0L, TimeUnit.MILLISECONDS,
                new LinkedBlockingQueue<>(properties.getReloadQueue()),
                new ThreadFactory() {
                    private AtomicInteger threadIndex = new AtomicInteger(0);

                    @Override
                    public Thread newThread(Runnable r) {
                        Thread thread = new Thread(r);
                        thread.setName("micro-cache-clear-task-" + (threadIndex.incrementAndGet()));
                        thread.setDaemon(true);
                        return thread;
                    }
                }, new ThreadPoolExecutor.DiscardPolicy());
    }

    /**
     * The get cache by {@link CacheKey}
     *
     * @param cacheKey {@link CacheKey}
     * @return {@link Cache}
     */
    public static Cache<Object, Object> getCache(CacheKey cacheKey) {
        return cacheMap.get(cacheKey);
    }

    /**
     * The build cache key
     *
     * @param microCache {@link MicroCache}
     * @param parameters cache parameter list
     * @return cache key
     */
    public static String buildKey(MicroCache microCache, Map<String, Object> parameters) {
        return buildKey(microCache.value(), MicroAuthContext.getMemberId(), microCache.keys(), parameters);
    }

    /**
     * The build evict cache key
     *
     * @param microEvictCache {@link MicroEvictCache}
     * @param parameters      cache parameter list
     * @return evict cache key
     */
    public static String buildEvictKey(MicroEvictCache microEvictCache, Map<String, Object> parameters) {
        return buildKey(microEvictCache.value(), MicroAuthContext.getMemberId(), microEvictCache.keys(), parameters);
    }

    /**
     * The build cache key
     *
     * @param cacheKey   {@link CacheKey}
     * @param memberId   memberId
     * @param keys       keys
     * @param parameters parameter list
     * @return cache key
     */
    public static String buildKey(CacheKey cacheKey, Object memberId, String[] keys, Map<String, Object> parameters) {
        MicroCacheRuleProperties microCacheRuleProperties = cacheRuleMap.get(cacheKey);
        if (microCacheRuleProperties == null) {
            return null;
        }
        StringBuilder sb = new StringBuilder(":" + cacheKey.name());
        if (microCacheRuleProperties.isToken()) {
            sb.append(":").append(memberId);
        }
        sb.append(buildKeySuffix(keys, parameters));
        return sb.toString();
    }

    /**
     * The delete cache by {@link CacheKey}
     *
     * @param cacheKey  {@link CacheKey}
     * @param memberIds member id list
     */
    public static void deleteCache(CacheKey cacheKey, Collection<? extends String> memberIds) {
        try {
            if (CollectionUtils.isEmpty(memberIds)) {
                return;
            }
            Cache<Object, Object> cache = getCache(cacheKey);
            if (cache == null) {
                return;
            }
            Map<String, String> deleteKeyMemberIds = new HashMap<>();
            for (String memberId : memberIds) {
                deleteKeyMemberIds.put(memberId, buildKey(cacheKey, memberId, null, null));
            }
            cache.removeAll(deleteKeyMemberIds.keySet());
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
    }

    @Override
    public void destroy() throws Exception {
        if (threadPoolExecutor != null) {
            threadPoolExecutor.shutdown();
        }
        for (Map.Entry<CacheKey, Cache<Object, Object>> entry : cacheMap.entrySet()) {
            if (entry.getValue() != null) {
                entry.getValue().close();
            }
        }
        if (redisClient != null) {
            redisClient.shutdown();
        }
    }

    /**
     * The build key suffix
     *
     * @param args       args
     * @param parameters parameter list
     * @return cache key suffix
     */
    private static String buildKeySuffix(String[] args, Map<String, Object> parameters) {
        if (args == null || args.length == 0) {
            return "";
        }

        StringBuilder sb = new StringBuilder(":");
        int length = args.length;
        for (int i = 0; i < length; i++) {
            sb.append(parameters.get(args[i]));
            if (i < length - 1) {
                sb.append("@");
            }
        }

        return sb.toString();
    }

    /**
     * The build cache
     *
     * @param microCacheRuleProperties {@link MicroCacheRuleProperties}
     * @return {@link Cache}
     */
    private Cache<Object, Object> buildCache(MicroCacheRuleProperties microCacheRuleProperties) {
        // local limit
        int localLimit = microCacheRuleProperties.getLimit();
        if (localLimit == 0) {
            localLimit = properties.getLocalLimit();
        }
        // local expire after write
        long localExpireMillis = microCacheRuleProperties.getLocalExpire().toMillis();
        if (localExpireMillis == 0) {
            localExpireMillis = properties.getLocalExpire().toMillis();
        }
        // remote expire after write
        long remoteExpireMillis = microCacheRuleProperties.getRemoteExpire().toMillis();
        if (remoteExpireMillis == 0) {
            remoteExpireMillis = properties.getRemoteExpire().toMillis();
        }

        switch (microCacheRuleProperties.getCacheType()) {
            case LOCAL:
                return this.buildCache(properties.getLocalType(), localExpireMillis, localLimit);
            case REMOTE:
                return this.buildCache(properties.getRemoteType(), remoteExpireMillis, localLimit);
            case BOTH:
                return this.buildCache(MicroCacheType.BOTH, remoteExpireMillis, localLimit);
            default:
                throw new MicroErrorException("Illegal Cache Type");
        }
    }

    /**
     * The build cache
     *
     * @param microCacheType   {@link MicroCacheType}
     * @param expireAfterWrite expireAfterWrite
     * @param localLimit       local limit
     * @return {@link Cache}
     */
    private Cache<Object, Object> buildCache(MicroCacheType microCacheType, long expireAfterWrite, Integer localLimit) {
        switch (microCacheType) {
            case LINKED_HASH_MAP:
                return this.buildLinkedHashMapCache(expireAfterWrite, TimeUnit.MILLISECONDS, localLimit);
            case REDIS_LETTUCE:
                return this.buildRedisLettuceCache(expireAfterWrite, TimeUnit.MILLISECONDS);
            case BOTH:
                Cache<Object, Object> level1Cache = this.buildLinkedHashMapCache(expireAfterWrite, TimeUnit.MILLISECONDS, localLimit);
                Cache<Object, Object> level2Cache = this.buildRedisLettuceCache(expireAfterWrite, TimeUnit.MILLISECONDS);
                return MultiLevelCacheBuilder.createMultiLevelCacheBuilder().addCache(level1Cache, level2Cache).buildCache();
            default:
                throw new IllegalArgumentException("Illegal micro cache type.");
        }
    }

    /**
     * The build redis lettuce cache
     *
     * @param localExpireAfterWrite local expire after write
     * @param timeUnit              {@link TimeUnit}
     * @param localLimit            local limit
     * @return {@link Cache}
     */
    private Cache<Object, Object> buildLinkedHashMapCache(long localExpireAfterWrite, TimeUnit timeUnit, int localLimit) {
        return LinkedHashMapCacheBuilder
                .createLinkedHashMapCacheBuilder()
                .expireAfterWrite(localExpireAfterWrite, timeUnit)
                .limit(localLimit)
                .buildCache();
    }

    /**
     * The build redis lettuce cache
     *
     * @param remoteExpireAfterWrite remote expire after write
     * @param timeUnit               {@link TimeUnit}
     * @return {@link Cache}
     */
    private Cache<Object, Object> buildRedisLettuceCache(long remoteExpireAfterWrite, TimeUnit timeUnit) {
        return RedisLettuceCacheBuilder.createRedisLettuceCacheBuilder()
                .keyConvertor(FastjsonKeyConvertor.INSTANCE)
                .valueEncoder(valueEncoder)
                .valueDecoder(valueDecoder)
                .redisClient(redisClient)
                .keyPrefix(properties.getKeyPrefix())
                .expireAfterWrite(remoteExpireAfterWrite, timeUnit)
                .buildCache();
    }

}