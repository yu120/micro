package cn.micro.biz.commons.exception;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * Abstract Micro Exception
 *
 * @author lry
 */
@Getter
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
public abstract class AbstractMicroException extends RuntimeException {

    protected Integer code;
    protected String message;

    protected String stack;

    public AbstractMicroException(Integer code, String message, String stack, Throwable cause) {
        super(message, cause);
        this.code = code;
        this.message = message;
        this.stack = stack;
    }

}
