package cn.micro.biz.commons.response;

import java.lang.annotation.*;

/**
 * The non wrapper meta annotation
 *
 * @author lry
 */
@Documented
@Inherited
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface NonMetaData {
}
