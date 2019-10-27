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

    /**
     * The event collect title
     *
     * @return title
     */
    String value();

    /**
     * The event collect describe
     *
     * @return describe
     */
    String desc() default "";

    /**
     * The event collect enable
     *
     * @return enable
     */
    boolean enable() default true;

    /**
     * The event collect async
     *
     * @return async
     */
    boolean async() default true;

    /**
     * The event collect advice
     *
     * @return advice {@link EventPost}
     */
    EventPost advice() default EventPost.AFTER;


    /**
     * Event Post
     *
     * @author lry
     */
    enum EventPost {

        // ===

        BEFORE, AFTER;

    }

}