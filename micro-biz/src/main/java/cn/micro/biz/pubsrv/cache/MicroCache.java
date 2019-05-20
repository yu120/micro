package cn.micro.biz.pubsrv.cache;

import java.lang.annotation.*;

/**
 * Micro Cache
 *
 * @author lry
 */
@Documented
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface MicroCache {

    CacheKey value();

    String[] keys() default {};

}