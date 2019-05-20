package cn.micro.biz.commons.auth;

import java.lang.annotation.*;

/**
 * The pre auth annotation
 *
 * @author lry
 */
@Documented
@Inherited
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface PreAuth {
}
