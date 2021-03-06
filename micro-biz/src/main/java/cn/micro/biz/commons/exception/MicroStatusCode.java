package cn.micro.biz.commons.exception;

import cn.micro.biz.commons.configuration.MicroSpringUtils;
import cn.micro.biz.commons.exception.support.*;
import cn.micro.biz.commons.response.MetaData;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.jdbc.BadSqlGrammarException;
import org.springframework.validation.BindException;
import org.springframework.validation.ObjectError;
import org.springframework.web.HttpMediaTypeNotSupportedException;
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
public enum MicroStatusCode {

    // ======= Micro Framework Exception   BindException.class

    SUCCESS(200, "Success", null),
    MICRO_BAD_REQUEST_EXCEPTION(400, "Bad Request", MicroBadRequestException.class),
    MICRO_PERMISSION_EXCEPTION(401, "Unauthorized", MicroPermissionException.class),
    MICRO_TOKEN_NOT_FOUND_EXCEPTION(402, "Not Logged On", MicroTokenNotFoundException.class),
    MICRO_TOKEN_EXPIRED_EXCEPTION(403, "Forbidden, Token Has Expired", MicroTokenExpiredException.class),
    MICRO_ERROR_EXCEPTION(500, "Internal Server Error", MicroErrorException.class),
    ABSTRACT_MICRO_EXCEPTION(null, null, AbstractMicroException.class) {
        @Override
        protected MetaData wrapper(Throwable t) {
            MetaData metaData = super.wrapper(t);
            AbstractMicroException ex = (AbstractMicroException) t;
            metaData.setCode(ex.getCode());
            metaData.setMessage(ex.getMessage());
            if (ex.getCode() == null) {
                throw new MicroErrorException("The 'code' value[" + ex.getCode() + "] must be set", t);
            }
            if (ex.getMessage() == null || ex.getMessage().length() == 0) {
                throw new MicroErrorException("The 'message' value[" + ex.getCode() + "] must be set", t);
            }
            if (ex.getCode() >= MIN_CODE) {
                throw new MicroErrorException("The 'code' value[" + ex.getCode() + "] must more than or equal to " + MIN_CODE, t);
            }
            if (ex.getCode() <= MAX_CODE) {
                throw new MicroErrorException("The 'code' value[" + ex.getCode() + "] must less than or equal to " + MAX_CODE, t);
            }

            return metaData;
        }
    },

    // ======= 3th Framework Exception

    BIND_EXCEPTION(400, "Bind Exception", BindException.class),
    CONSTRAINT_VIOLATION_EXCEPTION(400, "Bad Request Parameter", ConstraintViolationException.class) {
        @Override
        protected MetaData wrapper(Throwable t) {
            MetaData metaData = super.wrapper(t);
            Iterator<ConstraintViolation<?>> iterator = ((ConstraintViolationException) t).getConstraintViolations().iterator();
            if (iterator.hasNext()) {
                metaData.setMessage(iterator.next().getMessageTemplate());
            }

            return metaData;
        }
    },
    METHOD_ARGUMENT_NOT_VALID_EXCEPTION(400, "Method Argument Not Valid", MethodArgumentNotValidException.class) {
        @Override
        protected MetaData wrapper(Throwable t) {
            MetaData metaData = super.wrapper(t);
            Iterator<ObjectError> iterator = ((MethodArgumentNotValidException) t).getBindingResult().getAllErrors().iterator();
            if (iterator.hasNext()) {
                metaData.setMessage(iterator.next().getDefaultMessage());
            }

            return metaData;
        }
    },
    HTTP_MESSAGE_NOT_READABLE_EXCEPTION(400, "Message Not Readable", HttpMessageNotReadableException.class),
    NO_HANDLER_FOUND_EXCEPTION(404, "Not Found", NoHandlerFoundException.class),
    HTTP_REQUEST_METHOD_NOT_SUPPORTED_EXCEPTION(405, "Method Not Allowed", HttpRequestMethodNotSupportedException.class),
    HTTP_MEDIA_TYPE_NOT_SUPPORTED_EXCEPTION(405, "MediaType Not Supported", HttpMediaTypeNotSupportedException.class),
    METHOD_ARGUMENT_TYPE_MISMATCH_EXCEPTION(406, "Not Acceptable Argument Type", MethodArgumentTypeMismatchException.class),
    MAX_UPLOAD_SIZE_EXCEEDED_EXCEPTION(413, "Upload Max Exceeded", MaxUploadSizeExceededException.class),
    BAD_SQL_GRAMMAR_EXCEPTION(500, "Unknown Bad SQL Exception", BadSqlGrammarException.class) {
        @Override
        protected MetaData wrapper(Throwable t) {
            MetaData metaData = super.wrapper(t);
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

    private static final Integer MIN_CODE = 600;
    private static final Integer MAX_CODE = 999;

    private final Integer code;
    private final String message;
    private final Class<? extends Exception> error;

    MetaData wrapper(Throwable t) {
        return new MetaData(this.code, this.message, t.getMessage());
    }

    /**
     * The build success {@link MetaData}
     *
     * @param obj response body
     * @return {@link MetaData}
     */
    public static MetaData buildSuccess(Object obj) {
        return new MetaData(SUCCESS.getCode(), SUCCESS.getMessage(), obj);
    }

    /**
     * The build success {@link MetaData}
     *
     * @param exceptionDebug exception debug switch
     * @param t              {@link Throwable}
     * @return {@link MetaData}
     */
    public static String buildFailureJson(boolean exceptionDebug, Throwable t) {
        MetaData metaData = buildFailure(exceptionDebug, t);

        try {
            return MicroSpringUtils.getObjectMapper().writeValueAsString(metaData);
        } catch (JsonProcessingException e) {
            throw new MicroErrorException("Write to json exception", e);
        }
    }

    /**
     * The build success {@link MetaData}
     *
     * @param exceptionDebug exception debug switch
     * @param t              {@link Throwable}
     * @return {@link MetaData}
     */
    public static MetaData buildFailure(boolean exceptionDebug, Throwable t) {
        if (exceptionDebug) {
            log.error("Debug Exception", t);
        }

        MetaData metaData = null;
        for (MicroStatusCode microStatusCode : values()) {
            if (microStatusCode.getError() == null) {
                continue;
            }

            if (microStatusCode.getError().isAssignableFrom(t.getClass())) {
                metaData = microStatusCode.wrapper(t);
                break;
            }
        }

        // print internal server error
        if (!exceptionDebug) {
            if (metaData == null || metaData.getCode() >= MICRO_ERROR_EXCEPTION.getCode()) {
                log.error("Internal Server Error", t);
            }
        }
        if (metaData != null) {
            return metaData;
        }

        // Unknown Internal Server Error
        return new MetaData(MICRO_ERROR_EXCEPTION.getCode(),
                "Unknown Internal Server Error", t.getMessage());
    }

}
