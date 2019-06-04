package cn.micro.biz.commons.exception.support;

import cn.micro.biz.commons.exception.AbstractMicroException;
import lombok.EqualsAndHashCode;
import lombok.Getter;

/**
 * Micro Token Expired Exception
 *
 * @author lry
 */
@Getter
@EqualsAndHashCode(callSuper = false)
public class MicroTokenExpiredException extends AbstractMicroException {

    public MicroTokenExpiredException(String message) {
        super(null, message, null, null);
    }

}
