package cn.micro.biz.pubsrv.cache.support;

import cn.micro.biz.commons.exception.MicroCacheException;
import cn.micro.biz.pubsrv.cache.MicroCache;
import cn.micro.biz.pubsrv.cache.MicroCacheService;
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
import org.springframework.core.LocalVariableTableParameterNameDiscoverer;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

/**
 * 基于注解的AOP缓存
 *
 * @author lry
 */
@Slf4j
@Aspect
@Configuration
@ConditionalOnProperty(prefix = MicroCacheProperties.PREFIX, name = "enable", havingValue = "true")
public class MicroSpringCacheAop {

    @Pointcut("@annotation(cn.micro.biz.pubsrv.cache.MicroCache)")
    public void pointcutService() {
    }

    @Around("pointcutService()")
    public Object pjpService(ProceedingJoinPoint pjp) throws Throwable {
        try {
            Signature signature = pjp.getSignature();
            if (signature == null) {
                return pjp.proceed();
            }
            if (!(signature instanceof MethodSignature)) {
                return pjp.proceed();
            }
            Method method = ((MethodSignature) signature).getMethod();
            if (method == null) {
                return pjp.proceed();
            }

            MicroCache microCache = method.getAnnotation(MicroCache.class);
            Map<String, Object> parameters = parseParameters(pjp, method);
            Cache<Object, Object> tempCache = MicroCacheService.getCache(microCache.value());
            if (tempCache == null) {
                return pjp.proceed();
            }

            String key = MicroCacheService.buildKey(microCache, parameters);
            if (StringUtils.isNotBlank(key)) {
                return tempCache.computeIfAbsent(key, o -> {
                    try {
                        return pjp.proceed();
                    } catch (Throwable t) {
                        throw new MicroCacheException(t);
                    }
                });
            }
        } catch (MicroCacheException e) {
            throw e.getCause();
        } catch (Throwable t) {
            return pjp.proceed();
        }

        return pjp.proceed();
    }

    /**
     * Parse Parameters
     *
     * @param pjp    {@link ProceedingJoinPoint}
     * @param method {@link Method}
     * @return {@link Map<String, Object> }
     * @throws IllegalAccessException throw {@link IllegalAccessException}
     */
    public static Map<String, Object> parseParameters(ProceedingJoinPoint pjp, Method method) throws IllegalAccessException {
        Map<String, Object> parameters = new HashMap<>();
        LocalVariableTableParameterNameDiscoverer u = new LocalVariableTableParameterNameDiscoverer();
        String[] params = u.getParameterNames(method);
        if (params == null || params.length == 0) {
            return parameters;
        }
        Object[] args = pjp.getArgs();
        for (int i = 0; i < params.length; i++) {
            getAttribute(parameters, args[i], params[i]);
        }
        return parameters;
    }

    /**
     * 获取对象下面所有的属性名称+值
     *
     * @param parameters   {@link Map<String, Object> }
     * @param obj          object
     * @param objFieldName obj field name
     * @throws IllegalAccessException throw {@link IllegalAccessException}
     */
    private static void getAttribute(Map<String, Object> parameters, Object obj, String objFieldName) throws IllegalAccessException {
        if (obj == null || isPrimitive(obj)) {
            parameters.put(objFieldName, obj);
        } else {
            Field[] fields = obj.getClass().getDeclaredFields();
            for (Field field : fields) {
                field.setAccessible(true);
                getAttribute(parameters, field.get(obj), objFieldName + "." + field.getName());
            }
        }
    }

    /**
     * 判断是否为常规类型
     *
     * @param obj obj
     * @return true表示为常规对象
     */
    private static boolean isPrimitive(Object obj) {
        return obj instanceof String ||
                obj instanceof Number ||
                obj instanceof Boolean ||
                obj.getClass().isPrimitive();
    }

}
