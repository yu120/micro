package cn.micro.biz.commons.exception;

import lombok.EqualsAndHashCode;
import lombok.Getter;

/**
 * Micro Error Exception
 *
 * @author lry
 */
@Getter
@EqualsAndHashCode(callSuper = false)
public class MicroErrorException extends AbstractMicroException {

    public MicroErrorException(String message) {
        super(message, null, null);
    }

    public MicroErrorException(String message, Throwable cause) {
        super(message, null, cause);
    }

}
