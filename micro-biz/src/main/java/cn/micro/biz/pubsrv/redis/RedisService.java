package cn.micro.biz.pubsrv.redis;

import com.lambdaworks.redis.RedisClient;
import com.lambdaworks.redis.api.StatefulRedisConnection;
import com.lambdaworks.redis.api.sync.RedisCommands;
import com.lambdaworks.redis.support.ConnectionPoolSupport;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.pool2.impl.GenericObjectPool;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Slf4j
@Getter
@Service
@EnableConfigurationProperties(RedisProperties.class)
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class RedisService implements InitializingBean, DisposableBean {

    private final RedisProperties redisProperties;

    private static boolean enable = false;
    private static RedisClient lettuceRedisClient;
    private static GenericObjectPool<StatefulRedisConnection> genericObjectPool;

    @Override
    public void afterPropertiesSet() throws Exception {
        enable = redisProperties.isEnable();
        if (!enable) {
            return;
        }

        lettuceRedisClient = RedisClient.create(redisProperties.getUri());
        lettuceRedisClient.setDefaultTimeout(redisProperties.getTimeout(), TimeUnit.SECONDS);

        GenericObjectPoolConfig poolConfig = new GenericObjectPoolConfig();
        poolConfig.setMinIdle(redisProperties.getMinIdle());
        poolConfig.setMaxIdle(redisProperties.getMaxIdle());
        poolConfig.setMaxTotal(redisProperties.getMaxTotal());
        poolConfig.setJmxEnabled(false);
        genericObjectPool = ConnectionPoolSupport.createGenericObjectPool(lettuceRedisClient::connect, poolConfig);
    }

    @SuppressWarnings("unchecked")
    public static <K, V> StatefulRedisConnection<K, V> getConnection() throws Exception {
        return genericObjectPool.borrowObject();
    }

    public static void commandSetSec(String key, Object value, long seconds) throws Exception {
        if (!enable) {
            return;
        }

        StatefulRedisConnection<String, Object> statefulRedisConnection = getConnection();
        RedisCommands<String, Object> redisCommands = statefulRedisConnection.sync();
        redisCommands.setex(key, seconds, value);
    }

    public static void commandSet(String key, Object value, long milliseconds) throws Exception {
        if (!enable) {
            return;
        }

        StatefulRedisConnection<String, Object> statefulRedisConnection = getConnection();
        RedisCommands<String, Object> redisCommands = statefulRedisConnection.sync();
        redisCommands.psetex(key, milliseconds, value);
    }

    public static <V> V commandGet(String key) throws Exception {
        if (!enable) {
            return null;
        }

        StatefulRedisConnection<String, V> statefulRedisConnection = getConnection();
        RedisCommands<String, V> redisCommands = statefulRedisConnection.sync();
        return redisCommands.get(key);
    }

    @Override
    public void destroy() {
        if (lettuceRedisClient != null) {
            lettuceRedisClient.shutdown();
        }
    }

}
