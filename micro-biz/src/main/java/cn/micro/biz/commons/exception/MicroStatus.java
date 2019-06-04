package cn.micro.biz.commons.exception;

import cn.micro.biz.commons.response.MetaData;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.jdbc.BadSqlGrammarException;
import org.springframework.validation.ObjectError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.servlet.NoHandlerFoundException;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.sql.SQLException;
import java.util.Iterator;

/**
 * Micro Status
 *
 * @author lry
 */
@Slf4j
@Getter
@AllArgsConstructor
public enum MicroStatus {

    // ======= Micro Framework Exception

    MICRO_BAD_REQUEST_EXCEPTION(400, MicroBadRequestException.class, "Bad Request"),
    MICRO_PERMISSION_EXCEPTION(401, MicroPermissionException.class, "Unauthorized"),
    MICRO_SIGN_IN_EXCEPTION(403, MicroSignInException.class, "Token has expired"),
    MICRO_ERROR_EXCEPTION(500, MicroErrorException.class, "Internal Server Error"),
    ABSTRACT_MICRO_EXCEPTION(null, AbstractMicroException.class, null) {
        @Override
        protected MetaData wrapper(Object traceId, Throwable t) {
            MetaData metaData = super.wrapper(traceId, t);
            AbstractMicroException ex = (AbstractMicroException) t;
            metaData.setCode(ex.getCode());
            metaData.setMessage(ex.getMessage());
            if (ex.getCode() == null) {
                log.error("The 'code' value[" + ex.getCode() + "] must be set", t);
            }
            if (ex.getMessage() == null || ex.getMessage().length() == 0) {
                log.error("The 'message' value[" + ex.getCode() + "] must be set", t);
            }

            return metaData;
        }
    },

    // ======= 3th Framework Exception

    HTTP_MESSAGE_NOT_READABLE_EXCEPTION(400, HttpMessageNotReadableException.class, "Message Not Readable"),
    CONSTRAINT_VIOLATION_EXCEPTION(400, ConstraintViolationException.class, "Bad Request Parameter") {
        @Override
        protected MetaData wrapper(Object traceId, Throwable t) {
            MetaData metaData = super.wrapper(traceId, t);
            Iterator<ConstraintViolation<?>> iterator = ((ConstraintViolationException) t).getConstraintViolations().iterator();
            if (iterator.hasNext()) {
                metaData.setMessage(iterator.next().getMessageTemplate());
            }

            return metaData;
        }
    },
    METHOD_ARGUMENT_NOT_VALID_EXCEPTION(400, MethodArgumentNotValidException.class, "Method Argument Not Valid") {
        @Override
        protected MetaData wrapper(Object traceId, Throwable t) {
            MetaData metaData = super.wrapper(traceId, t);
            Iterator<ObjectError> iterator = ((MethodArgumentNotValidException) t).getBindingResult().getAllErrors().iterator();
            if (iterator.hasNext()) {
                metaData.setMessage(iterator.next().getDefaultMessage());
            }

            return metaData;
        }
    },
    NO_HANDLER_FOUND_EXCEPTION(404, NoHandlerFoundException.class, "Not Found Handler"),
    HTTP_REQUEST_METHOD_NOT_SUPPORTED_EXCEPTION(405, HttpRequestMethodNotSupportedException.class, "Method Not Allowed"),
    METHOD_ARGUMENT_TYPE_MISMATCH_EXCEPTION(406, MethodArgumentTypeMismatchException.class, "Not Acceptable Argument Type"),
    MAX_UPLOAD_SIZE_EXCEEDED_EXCEPTION(413, MaxUploadSizeExceededException.class, "Payload Too Large"),
    BAD_SQL_GRAMMAR_EXCEPTION(500, BadSqlGrammarException.class, "Unknown Bad SQL Exception") {
        @Override
        protected MetaData wrapper(Object traceId, Throwable t) {
            MetaData metaData = super.wrapper(traceId, t);
            if (t.getCause() != null) {
                // SQL Syntax Error Exception
                if (t.getCause() instanceof SQLException) {
                    SQLException se = ((SQLException) t.getCause());
                    metaData.setMessage("Bad SQL Exception");
                    metaData.setStack(String.format("Bad SQL[Code:%s(State:%s)] %s",
                            se.getErrorCode(), se.getSQLState(), se.getMessage()));
                }
            }

            return metaData;
        }
    };

    private final Integer code;
    private final Class<? extends Exception> error;
    private final String message;

    protected MetaData wrapper(Object traceId, Throwable t) {
        return MetaData.build(traceId, this.code, this.message, t.getMessage());
    }

    public static MetaData buildErrorMetaData(boolean exceptionDebug, Object traceId, Throwable t) {
        if (exceptionDebug) {
            log.error("Internal Server Error", t);
        }

        MetaData metaData = null;
        for (MicroStatus microStatus : values()) {
            if (microStatus.getError().isAssignableFrom(t.getClass())) {
                metaData = microStatus.wrapper(traceId, t);
                break;
            }
        }

        // print internal server error
        if (!exceptionDebug) {
            if (metaData == null || metaData.getCode() >= MICRO_ERROR_EXCEPTION.getCode()) {
                log.error("Internal Server Error", t);
            }
        }

        // Unknown Internal Server Error
        return MetaData.build(traceId, 500, "Unknown Internal Server Error", t.getMessage());
    }

}
