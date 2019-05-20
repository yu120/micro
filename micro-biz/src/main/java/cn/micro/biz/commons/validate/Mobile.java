package cn.micro.biz.commons.validate;

import org.hibernate.validator.constraints.CompositionType;
import org.hibernate.validator.constraints.ConstraintComposition;
import org.hibernate.validator.constraints.Length;

import javax.validation.Constraint;
import javax.validation.Payload;
import javax.validation.ReportAsSingleViolation;
import javax.validation.constraints.Null;
import javax.validation.constraints.Pattern;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * The Mobile Validator Annotation
 *
 * @author lry
 */
@ConstraintComposition(CompositionType.OR)
@Pattern(regexp = "^((13[0-9])|(14[5|7])|(15([0-3]|[5-9]))|(18[0,1,2,5-9])|(177))\\d{8}$")
@Null
@Length(max = 0)
@Documented
@Constraint(validatedBy = {})
@Target({ElementType.METHOD, ElementType.FIELD, ElementType.CONSTRUCTOR, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@ReportAsSingleViolation
public @interface Mobile {

    /**
     * 是否允许为空
     *
     * @return false表示不允许为空
     */
    boolean allowNull() default false;

    /**
     * 是否验证电话号码
     *
     * @return true表示校验电话号码
     */
    boolean tel() default false;

    String message() default "Illegal format mobile";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

}
