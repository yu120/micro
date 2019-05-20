package cn.micro.biz.commons.exception;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import org.springframework.http.HttpStatus;

/**
 * Micro Not Permission Exception
 *
 * @author lry
 */
@Getter
@EqualsAndHashCode(callSuper = false)
public class MicroPermissionException extends AbstractMicroException {

    private static final int CODE_VALUE = HttpStatus.UNAUTHORIZED.value();

    public MicroPermissionException(String message) {
        super(CODE_VALUE, message);
    }

}
