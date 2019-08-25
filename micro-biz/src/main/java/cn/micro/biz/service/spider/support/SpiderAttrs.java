package cn.micro.biz.service.spider.support;

import java.lang.annotation.*;

/**
 * Spider Goods Attribute List
 *
 * @author lry
 */
@Documented
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface SpiderAttrs {

    SpiderAttr[] value();

}
