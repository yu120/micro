package cn.micro.biz.commons.exception;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import org.springframework.http.HttpStatus;

/**
 * Micro Not Sign In Exception
 *
 * @author lry
 */
@Getter
@EqualsAndHashCode(callSuper = false)
public class MicroSignInException extends AbstractMicroException {

    private static final int CODE_VALUE = HttpStatus.PAYMENT_REQUIRED.value();

    public MicroSignInException(String message) {
        super(CODE_VALUE, message);
    }

    public MicroSignInException(int code, String message) {
        super(code, message);
    }

}
