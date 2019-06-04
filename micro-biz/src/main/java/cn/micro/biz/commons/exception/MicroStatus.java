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
    MICRO_SIGN_IN_EXCEPTION(403, MicroSignInException.class, "Bad Request"),
    MICRO_ERROR_EXCEPTION(500, MicroErrorException.class, "Internal Server Error"),
    ABSTRACT_MICRO_EXCEPTION(null, AbstractMicroException.class, null) {
        @Override
        protected MetaData build(Object traceId, Exception e) {
            MetaData metaData = super.build(traceId, e);
            AbstractMicroException ex = (AbstractMicroException) e;
            metaData.setCode(ex.getCode());
            metaData.setMessage(ex.getMessage());
            if (ex.getCode() == null) {
                log.error("The 'code' value[" + ex.getCode() + "] must be set", e);
            }
            if (ex.getMessage() == null || ex.getMessage().length() == 0) {
                log.error("The 'message' value[" + ex.getCode() + "] must be set", e);
            }

            return metaData;
        }
    },

    // ======= 3th Framework Exception

    HTTP_MESSAGE_NOT_READABLE_EXCEPTION(400, HttpMessageNotReadableException.class, "Message Not Readable"),
    CONSTRAINT_VIOLATION_EXCEPTION(400, ConstraintViolationException.class, "Bad Request Parameter") {
        @Override
        protected MetaData build(Object traceId, Exception e) {
            MetaData metaData = super.build(traceId, e);
            Iterator<ConstraintViolation<?>> iterator = ((ConstraintViolationException) e).getConstraintViolations().iterator();
            if (iterator.hasNext()) {
                metaData.setMessage(iterator.next().getMessageTemplate());
            }

            return metaData;
        }
    },
    METHOD_ARGUMENT_NOT_VALID_EXCEPTION(400, MethodArgumentNotValidException.class, "Method Argument Not Valid") {
        @Override
        protected MetaData build(Object traceId, Exception e) {
            MetaData metaData = super.build(traceId, e);
            Iterator<ObjectError> iterator = ((MethodArgumentNotValidException) e).getBindingResult().getAllErrors().iterator();
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
        protected MetaData build(Object traceId, Exception e) {
            MetaData metaData = super.build(traceId, e);
            if (e.getCause() != null) {
                // SQL Syntax Error Exception
                if (e.getCause() instanceof SQLException) {
                    SQLException se = ((SQLException) e.getCause());
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

    protected MetaData build(Object traceId, Exception e) {
        return MetaData.build(traceId, this.code, this.message, e.getMessage());
    }

    public static MetaData buildErrorMetaData(Object traceId, Exception e) {
        for (MicroStatus microStatus : values()) {
            if (microStatus.getError().isAssignableFrom(e.getClass())) {
                return microStatus.build(traceId, e);
            }
        }

        // Unknown Internal Server Error
        return MetaData.build(traceId, 500, "Unknown Internal Server Error", e.getMessage());
    }

}
