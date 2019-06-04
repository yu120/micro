package cn.micro.biz.commons.exception;

import lombok.EqualsAndHashCode;
import lombok.Getter;

/**
 * Micro Not Permission Exception
 *
 * @author lry
 */
@Getter
@EqualsAndHashCode(callSuper = false)
public class MicroPermissionException extends AbstractMicroException {

    public MicroPermissionException(String message) {
        super(message, null, null);
    }

}
