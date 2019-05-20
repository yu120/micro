package cn.micro.biz.pubsrv.cache.support;

import cn.micro.biz.pubsrv.cache.MicroCacheService;
import cn.micro.biz.pubsrv.cache.MicroEvictCache;
import com.alicp.jetcache.Cache;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Configuration;

import java.lang.reflect.Method;
import java.util.Map;

/**
 * 基于注解的AOP清理缓存
 *
 * @author lry
 */
@Slf4j
@Aspect
@Configuration
@ConditionalOnProperty(prefix = MicroCacheProperties.PREFIX, name = "enable", havingValue = "true")
public class MicroSpringEvictCacheAop {

    @Pointcut("@annotation(cn.micro.biz.pubsrv.cache.MicroEvictCache)")
    public void pointcutService() {
    }

    @Around("pointcutService()")
    public Object pjpService(ProceedingJoinPoint pjp) throws Throwable {
        try {
            return pjp.proceed();
        } finally {
            try {
                Signature signature = pjp.getSignature();
                if (signature != null) {
                    if (signature instanceof MethodSignature) {
                        Method method = ((MethodSignature) signature).getMethod();
                        if (method != null) {
                            MicroEvictCache microEvictCache = method.getAnnotation(MicroEvictCache.class);
                            Map<String, Object> parameters = MicroSpringCacheAop.parseParameters(pjp, method);
                            Cache<Object, Object> tempCache = MicroCacheService.getCache(microEvictCache.value());
                            if (tempCache != null) {
                                String key = MicroCacheService.buildEvictKey(microEvictCache, parameters);
                                if (StringUtils.isNotBlank(key)) {
                                    tempCache.remove(key);
                                }
                            }
                        }
                    }
                }
            } catch (Throwable t) {
                log.warn("Evict cache fail", t);
            }
        }
    }

}
