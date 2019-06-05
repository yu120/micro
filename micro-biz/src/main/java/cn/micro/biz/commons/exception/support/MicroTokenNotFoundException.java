package cn.micro.biz.commons.exception.support;

import cn.micro.biz.commons.exception.AbstractMicroException;
import lombok.EqualsAndHashCode;
import lombok.Getter;

/**
 * Micro Token Not Found Exception
 *
 * @author lry
 */
@Getter
@EqualsAndHashCode(callSuper = false)
public class MicroTokenNotFoundException extends AbstractMicroException {

    public MicroTokenNotFoundException() {
        super(null, null, null, null);
    }

}
