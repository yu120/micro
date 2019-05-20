package cn.micro.biz.commons.trace;

import java.lang.annotation.*;

/**
 * The Trace Annotation
 *
 * @author lry
 */

@Documented
@Inherited
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Trace {
}
