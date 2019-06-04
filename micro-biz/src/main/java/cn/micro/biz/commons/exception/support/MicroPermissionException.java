package cn.micro.biz.commons.exception.support;

import cn.micro.biz.commons.exception.AbstractMicroException;
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
        super(null, message, null, null);
    }

}
