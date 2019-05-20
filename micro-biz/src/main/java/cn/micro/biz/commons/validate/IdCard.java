package cn.micro.biz.commons.validate;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

/**
 * The IdCard Validation Annotation
 *
 * @author lry
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD, ElementType.METHOD, ElementType.ANNOTATION_TYPE})
@Constraint(validatedBy = IdCardValidator.class)
public @interface IdCard {

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
     * 校验失败时提示信息
     *
     * @return message
     */
    String message() default "Illegal IdCard";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

}