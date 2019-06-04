package cn.micro.biz.commons.exception;

import lombok.EqualsAndHashCode;
import lombok.Getter;

/**
 * Abstract Micro Exception
 *
 * @author lry
 */
@Getter
@EqualsAndHashCode(callSuper = false)
public abstract class AbstractMicroException extends RuntimeException {

    private Integer code;
    private String message;
    private String stack;

    public AbstractMicroException(Integer code, String message, String stack, Throwable cause) {
        super(message, cause);
        this.code = code;
        this.message = message;
        this.stack = stack;
    }

}
