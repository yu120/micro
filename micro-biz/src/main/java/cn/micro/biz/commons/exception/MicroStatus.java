package cn.micro.biz.commons.exception;

import cn.micro.biz.commons.response.MetaData;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import lombok.AllArgsConstructor;
import lombok.Getter;
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
@Getter
@AllArgsConstructor
public enum MicroStatus {

    // ======= Micro Framework Exception

    MICRO_BAD_REQUEST_EXCEPTION(400, MicroBadRequestException.class, "Bad Request"),
    MICRO_ERROR_EXCEPTION(400, MicroErrorException.class, "Bad Request"),
    MICRO_PERMISSION_EXCEPTION(401, MicroPermissionException.class, "Unauthorized"),
    MICRO_SIGN_IN_EXCEPTION(400, MicroSignInException.class, "Bad Request"),
    ABSTRACT_MICRO_EXCEPTION(400, AbstractMicroException.class, "Bad Request") {
        @Override
        protected MetaData build(Object traceId, Exception e) {
            MetaData metaData = super.build(traceId, e);
            if (e instanceof AbstractMicroException) {
                AbstractMicroException ex = (AbstractMicroException) e;
                metaData.setCode(ex.getCode());
                metaData.setMessage(ex.getMessage());
            }

            return metaData;
        }
    },

    // ======= 3th Framework Exception

    NO_HANDLER_FOUND_EXCEPTION(400, NoHandlerFoundException.class, "Bad Request"),
    MAX_UPLOAD_SIZE_EXCEEDED_EXCEPTION(400, MaxUploadSizeExceededException.class, "Upload file size should not exceed 1M"),
    METHOD_ARGUMENT_TYPE_MISMATCH_EXCEPTION(400, MethodArgumentTypeMismatchException.class, "Illegal Argument Type"),
    HTTP_REQUEST_METHOD_NOT_SUPPORTED_EXCEPTION(405, HttpRequestMethodNotSupportedException.class, "Method Not Allowed"),
    CONSTRAINT_VIOLATION_EXCEPTION(400, ConstraintViolationException.class, "Bad Request") {
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
    METHOD_ARGUMENT_NOT_VALID_EXCEPTION(400, MethodArgumentNotValidException.class, "Bad Request") {
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
    HTTP_MESSAGE_NOT_READABLE_EXCEPTION(400, HttpMessageNotReadableException.class, "Message Not Readable") {
        @Override
        protected MetaData build(Object traceId, Exception e) {
            MetaData metaData = super.build(traceId, e);
            if (e.getCause() != null) {
                if (e.getCause() instanceof JsonParseException
                        || e.getCause() instanceof com.google.gson.JsonParseException
                        || e.getCause() instanceof org.springframework.boot.json.JsonParseException) {
                    metaData.setMessage("Illegal Body by JSON Parse Fail");
                    return metaData;
                } else if (e.getCause() instanceof InvalidFormatException) {
                    if (e.getMessage().contains("Cannot deserialize value of type")) {
                        metaData.setMessage("Illegal value type cannot be deserialize");
                        return metaData;
                    }
                }
            }

            if (e.getMessage().contains("JSON parse error")) {
                metaData.setMessage("JSON parse error");
            }

            return metaData;
        }
    },
    BAD_SQL_GRAMMAR_EXCEPTION(400, BadSqlGrammarException.class, "Unknown Bad SQL Exception") {
        @Override
        protected MetaData build(Object traceId, Exception e) {
            MetaData metaData = super.build(traceId, e);
            if (e.getCause() != null) {
                // SQL Syntax Error Exception
                if (e.getCause() instanceof SQLException) {
                    SQLException se = ((SQLException) e.getCause());
                    metaData.setMessage("Bad SQL Exception");
                    metaData.setStack(String.format("Bad SQL[Code:%s(State:%s)] %s", se.getErrorCode(), se.getSQLState(), se.getMessage()));
                }
            }

            return metaData;
        }
    };

    private final int code;
    private final Class<? extends Exception> error;
    private final String message;

    protected MetaData build(Object traceId, Exception e) {
        return MetaData.build(traceId, code, message, e.getMessage());
    }

    public static MetaData buildMetaData(Object traceId, Exception e) {
        for (MicroStatus microStatus : values()) {
            if (microStatus.getError().isAssignableFrom(e.getClass())) {
                return microStatus.build(traceId, e);
            }
        }

        // Unknown Internal Server Error
        return MetaData.build(traceId, 500, "Internal Server Error", e.getMessage());
    }

}
