package cn.micro.biz.commons.exception;

import lombok.EqualsAndHashCode;
import lombok.Getter;

/**
 * Micro Cache Exception
 *
 * @author lry
 */
@Getter
@EqualsAndHashCode(callSuper = false)
public class MicroCacheException extends AbstractMicroException {

    public MicroCacheException(Throwable exception) {
        super(null, null, null, exception);
    }

}
