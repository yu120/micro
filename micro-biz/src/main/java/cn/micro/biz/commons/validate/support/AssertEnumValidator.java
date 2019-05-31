package cn.micro.biz.commons.validate.support;

import cn.micro.biz.commons.validate.AssertEnum;
import lombok.extern.slf4j.Slf4j;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.lang.reflect.Method;
import java.util.*;

/**
 * Enumeration Value Check Annotation Implementation
 *
 * @author lry
 */
@Slf4j
public class AssertEnumValidator implements ConstraintValidator<AssertEnum, Object> {

    private boolean allowNull;
    private Set<Object> values = new HashSet<>();

    @Override
    public void initialize(AssertEnum assertEnum) {
        this.allowNull = assertEnum.allowNull();
        Class<?> clz = assertEnum.value();
        Object[] objects = clz.getEnumConstants();

        try {
            Method method = clz.getMethod(assertEnum.name());
            if (Objects.isNull(method)) {
                throw new IllegalArgumentException("No found method: " + assertEnum.name());
            }

            for (Object obj : objects) {
                values.add(method.invoke(obj));
            }
        } catch (Exception e) {
            log.error("[处理枚举校验异常]", e);
        }
    }

    @Override
    public boolean isValid(Object value, ConstraintValidatorContext constraintValidatorContext) {
        if (allowNull) {
            return value == null || values.contains(value);
        } else {
            return value != null && values.contains(value);
        }
    }

}