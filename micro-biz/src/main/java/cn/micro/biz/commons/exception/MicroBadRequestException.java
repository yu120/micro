package cn.micro.biz.commons.exception;

import lombok.EqualsAndHashCode;
import lombok.Getter;

/**
 * Micro Bad Request Exception
 *
 * @author lry
 */
@Getter
@EqualsAndHashCode(callSuper = false)
public class MicroBadRequestException extends AbstractMicroException {

    public MicroBadRequestException(String message) {
        super(message, null, null);
    }

}
