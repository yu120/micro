package org.micro.extension;

import java.lang.annotation.*;

/**
 * SPI Extension
 *
 * @author lry
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface Extension {

    /**
     * Extension ID
     **/
    String value() default "";

    /**
     * Extension order value
     * <p>
     * The smaller the order number, the higher the position in the returned list.
     */
    int order() default 1;

    /**
     * SPI category, matching according to category when obtaining SPI list
     * <p>
     * When there is a search-category to be filtered in category, the matching is successful.
     */
    String[] category() default "";

}
