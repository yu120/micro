package cn.micro.biz.commons.exception;

import cn.micro.biz.commons.configuration.MicroProperties;
import cn.micro.biz.commons.response.MetaData;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.jdbc.BadSqlGrammarException;
import org.springframework.validation.ObjectError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.servlet.NoHandlerFoundException;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.sql.SQLException;
import java.util.Iterator;

/**
 * Exception Filter
 *
 * @author lry
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @Resource
    private MicroProperties microProperties;

    @ResponseBody
    @ExceptionHandler(value = Throwable.class)
    public MetaData defaultErrorHandler(HttpServletRequest request, Exception e) throws Exception {
        if (microProperties.isShowAllError()) {
            log.error("Internal Server Error", e);
        }
        String stack = e.getMessage();
        Object traceId = request.getAttribute(GlobalExceptionFilter.X_TRACE_ID);

        // AbstractMicroException
        if (e instanceof AbstractMicroException) {
            AbstractMicroException ex = (AbstractMicroException) e;
            return MetaData.build(traceId, ex.getCode(), ex.getMessage(), stack);
        }

        // NoHandlerFoundException
        if (e instanceof NoHandlerFoundException) {
            return MetaData.build(traceId, HttpStatus.BAD_REQUEST.value(), "Bad Request", stack);
        }
        
        // MaxUploadSizeExceededException
        if (e instanceof MaxUploadSizeExceededException) {
            return MetaData.build(traceId, HttpStatus.BAD_REQUEST.value(), "Upload file size should not exceed 1M", stack);
        }

        // ConstraintViolationException
        if (e instanceof ConstraintViolationException) {
            Iterator<ConstraintViolation<?>> iterator = ((ConstraintViolationException) e).getConstraintViolations().iterator();
            if (iterator.hasNext()) {
                return MetaData.build(traceId, HttpStatus.BAD_REQUEST.value(), iterator.next().getMessageTemplate(), stack);
            }
            return MetaData.build(traceId, HttpStatus.BAD_REQUEST.value(), "Bad Request", stack);
        }

        // MethodArgumentNotValidException
        if (e instanceof MethodArgumentNotValidException) {
            Iterator<ObjectError> iterator = ((MethodArgumentNotValidException) e).getBindingResult().getAllErrors().iterator();
            if (iterator.hasNext()) {
                return MetaData.build(traceId, HttpStatus.BAD_REQUEST.value(), iterator.next().getDefaultMessage(), null);
            }
            return MetaData.build(traceId, HttpStatus.BAD_REQUEST.value(), "Bad Request", stack);
        }

        // HttpMessageNotReadableException
        if (e instanceof HttpMessageNotReadableException) {
            if (e.getCause() != null) {
                if (e.getCause() instanceof JsonParseException
                        || e.getCause() instanceof com.google.gson.JsonParseException
                        || e.getCause() instanceof org.springframework.boot.json.JsonParseException) {
                    return MetaData.build(traceId, HttpStatus.BAD_REQUEST.value(), "Illegal Body by JSON Parse Fail", stack);
                } else if (e.getCause() instanceof InvalidFormatException) {
                    if (e.getMessage().contains("Cannot deserialize value of type")) {
                        return MetaData.build(traceId, HttpStatus.BAD_REQUEST.value(), "Illegal value type cannot be deserialize", stack);
                    }
                }
            }

            if (e.getMessage().contains("JSON parse error")) {
                return MetaData.build(traceId, HttpStatus.BAD_REQUEST.value(), "JSON parse error", stack);
            }

            return MetaData.build(traceId, HttpStatus.BAD_REQUEST.value(), "Message Not Readable", stack);
        }

        // MethodArgumentTypeMismatchException
        if (e instanceof MethodArgumentTypeMismatchException) {
            return MetaData.build(traceId, HttpStatus.BAD_REQUEST.value(), "Illegal Argument Type", stack);
        }

        // HttpRequestMethodNotSupportedException
        if (e instanceof HttpRequestMethodNotSupportedException) {
            return MetaData.build(traceId, HttpStatus.METHOD_NOT_ALLOWED.value(), "Method Not Allowed", null);
        }

        // print internal server error
        if (!microProperties.isShowAllError()) {
            log.error("Internal Server Error", e);
        }

        // BadSqlGrammarException
        if (e instanceof BadSqlGrammarException) {
            if (e.getCause() != null) {
                // SQL Syntax Error Exception
                if (e.getCause() instanceof SQLException) {
                    SQLException se = ((SQLException) e.getCause());
                    stack = String.format("Bad SQL[Code:%s(State:%s)] %s", se.getErrorCode(), se.getSQLState(), se.getMessage());
                    return MetaData.build(traceId, HttpStatus.INTERNAL_SERVER_ERROR.value(), "Bad SQL Exception", stack);
                }
            }

            return MetaData.build(traceId, HttpStatus.INTERNAL_SERVER_ERROR.value(), "Unknown Bad SQL Exception", stack);
        }

        // Unknown Internal Server Error
        return MetaData.build(traceId, HttpStatus.INTERNAL_SERVER_ERROR.value(), "Internal Server Error", stack);
    }

}