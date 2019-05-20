package cn.micro.biz.pubsrv.cache;

import java.lang.annotation.*;

/**
 * Micro Evict Cache
 *
 * @author lry
 */
@Documented
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface MicroEvictCache {

    CacheKey value();

    String[] keys() default {};

}