package cn.micro.biz.commons.validate;

import cn.micro.biz.commons.validate.support.AssertEnumValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

/**
 * The Not In Enum Validation Annotation
 *
 * @author lry
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD, ElementType.METHOD, ElementType.ANNOTATION_TYPE})
@Constraint(validatedBy = AssertEnumValidator.class)
public @interface AssertEnum {

    /**
     * 对比枚举类型
     *
     * @return Enum Class
     */
    Class<? extends Enum> value();

    /**
     * 是否允许为空
     *
     * @return false表示不允许为空
     */
    boolean allowNull() default false;

    /**
     * 校验值获取方法名称
     *
     * @return 默认使用value字段
     */
    String name() default "getValue";

    /**
     * 校验失败时提示信息
     *
     * @return message
     */
    String message() default "Illegal enum of parameter values";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

}