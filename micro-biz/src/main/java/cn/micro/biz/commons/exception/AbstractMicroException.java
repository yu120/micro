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

    protected int code;
    protected String message;

    protected String stack;

    public AbstractMicroException(int code, String message) {
        this(code, message, null);
    }

    public AbstractMicroException(int code, String message, Throwable cause) {
        this(code, message, null, cause);
    }

    public AbstractMicroException(int code, String message, String stack, Throwable cause) {
        super(message, cause);
        this.code = code;
        this.message = message;
        this.stack = stack;
    }

}
