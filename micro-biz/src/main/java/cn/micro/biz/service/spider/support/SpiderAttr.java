package cn.micro.biz.service.spider.support;

import java.lang.annotation.*;

/**
 * Spider Goods Attribute
 *
 * @author lry
 */
@Documented
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface SpiderAttr {

    SpiderApp app();

    String value();

    boolean isUrl() default false;

}
