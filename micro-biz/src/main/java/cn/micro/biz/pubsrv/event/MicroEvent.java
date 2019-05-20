package cn.micro.biz.pubsrv.event;

import java.lang.annotation.*;

/**
 * Micro Event
 *
 * @author lry
 */
@Documented
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface MicroEvent {

    MicroEventAction value();

    String desc() default "";

}