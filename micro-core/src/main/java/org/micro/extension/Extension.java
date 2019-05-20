package org.micro.extension;

import java.lang.annotation.*;

/**
 * NPI有多个实现时，可以根据条件进行过滤、排序后再返回。
 *
 * @author lry
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
public @interface Extension {

    /**
     * 自定义实现类ID
     **/
    String value() default "";

    /**
     * order号越小，在返回的list<Instance>中的位置越靠前
     */
    int order() default 20;

    /**
     * NPI的category，获取NPI列表时，根据category进行匹配
     * <p>
     * 当category中存在待过滤的search-category时，匹配成功
     */
    String[] category() default "";

}
