package cn.micro.biz.commons.exception.support;

import cn.micro.biz.commons.exception.AbstractMicroException;
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

    public MicroBadRequestException(String stack) {
        super(null, null, stack, null);
    }

}
