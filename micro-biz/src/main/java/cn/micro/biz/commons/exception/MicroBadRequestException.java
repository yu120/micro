package cn.micro.biz.commons.exception;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import org.springframework.http.HttpStatus;

/**
 * Micro Bad Request Exception
 *
 * @author lry
 */
@Getter
@EqualsAndHashCode(callSuper = false)
public class MicroBadRequestException extends AbstractMicroException {

    private static final int CODE_VALUE = HttpStatus.BAD_REQUEST.value();

    public MicroBadRequestException(String message) {
        super(CODE_VALUE, message);
    }

}
