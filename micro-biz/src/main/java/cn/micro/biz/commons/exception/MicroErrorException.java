package cn.micro.biz.commons.exception;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import org.springframework.http.HttpStatus;

/**
 * Micro Error Exception
 *
 * @author lry
 */
@Getter
@EqualsAndHashCode(callSuper = false)
public class MicroErrorException extends AbstractMicroException {

    private static final int CODE_VALUE = HttpStatus.INTERNAL_SERVER_ERROR.value();

    public MicroErrorException(String message) {
        super(CODE_VALUE, message);
    }

    public MicroErrorException(String message, Throwable cause) {
        super(CODE_VALUE, message, cause);
    }

}
