package cn.micro.biz.commons.validate.support;

import cn.micro.biz.commons.exception.support.MicroErrorException;
import cn.micro.biz.commons.validate.Length;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * Check that the character sequence length is between min and max.
 *
 * @author lry
 */
public class LengthValidator implements ConstraintValidator<Length, CharSequence> {

    private int min;
    private int max;
    private int length;

    @Override
    public void initialize(Length parameters) {
        min = parameters.min();
        max = parameters.max();
        length = parameters.length();
        if (min < 0) {
            throw new MicroErrorException("The min parameter cannot be negative.");
        }
        if (max < 0) {
            throw new MicroErrorException("The max parameter cannot be negative.");
        }
        if (max < min) {
            throw new MicroErrorException("The length cannot be negative.");
        }
    }

    @Override
    public boolean isValid(CharSequence value, ConstraintValidatorContext constraintValidatorContext) {
        if (value == null) {
            return true;
        }

        int tempLength = value.length();
        if (length > 0) {
            return tempLength == length;
        } else {
            return tempLength >= min && tempLength <= max;
        }
    }

}
