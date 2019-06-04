package cn.micro.biz.commons.exception.support;

import cn.micro.biz.commons.exception.AbstractMicroException;
import lombok.EqualsAndHashCode;
import lombok.Getter;

/**
 * Micro Not Sign In Exception
 *
 * @author lry
 */
@Getter
@EqualsAndHashCode(callSuper = false)
public class MicroSignInException extends AbstractMicroException {

    public MicroSignInException(String message) {
        super(null, message, null, null);
    }

}
