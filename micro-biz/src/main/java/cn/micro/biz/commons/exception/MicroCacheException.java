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

    private Throwable exception;

    public MicroCacheException(Throwable exception) {
        this.exception = exception;
    }

}
