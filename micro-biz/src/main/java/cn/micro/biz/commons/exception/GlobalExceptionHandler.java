package cn.micro.biz.commons.exception;

import cn.micro.biz.commons.response.MetaData;
import com.fasterxml.jackson.core.JsonParseException;
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

    @ResponseBody
    @ExceptionHandler(value = Throwable.class)
    public MetaData defaultErrorHandler(HttpServletRequest request, Exception e) throws Exception {
        Object traceId = request.getAttribute(GlobalExceptionFilter.X_TRACE_ID);

        // Custom exception
        if (e instanceof AbstractMicroException) {
            AbstractMicroException ex = (AbstractMicroException) e;
            return MetaData.build(traceId, ex.getCode(), ex.getMessage(), e.getMessage());
        }

        // Third party exception
        if (e instanceof NoHandlerFoundException) {
            return MetaData.build(traceId, HttpStatus.BAD_REQUEST.value(), "Bad Request", e.getMessage());
        }
        if (e instanceof MaxUploadSizeExceededException) {
            return MetaData.build(traceId, HttpStatus.BAD_REQUEST.value(), "Bad Request", "Upload file size should not exceed 1M");
        }
        if (e instanceof ConstraintViolationException) {
            Iterator<ConstraintViolation<?>> iterator = ((ConstraintViolationException) e).getConstraintViolations().iterator();
            if (iterator.hasNext()) {
                return MetaData.build(traceId, HttpStatus.BAD_REQUEST.value(), iterator.next().getMessageTemplate(), e.getMessage());
            }
            return MetaData.build(traceId, HttpStatus.BAD_REQUEST.value(), "Bad Request", e.getMessage());
        }
        if (e instanceof MethodArgumentNotValidException) {
            Iterator<ObjectError> iterator = ((MethodArgumentNotValidException) e).getBindingResult().getAllErrors().iterator();
            if (iterator.hasNext()) {
                return MetaData.build(traceId, HttpStatus.BAD_REQUEST.value(), iterator.next().getDefaultMessage(), null);
            }
            return MetaData.build(traceId, HttpStatus.BAD_REQUEST.value(), "Bad Request", e.getMessage());
        }
        // Illegal Body by JSON Parse Fail
        if (e instanceof HttpMessageNotReadableException) {
            if (e.getCause() != null) {
                if (e.getCause() instanceof JsonParseException
                        || e.getCause() instanceof com.google.gson.JsonParseException
                        || e.getCause() instanceof org.springframework.boot.json.JsonParseException) {
                    return MetaData.build(traceId, HttpStatus.BAD_REQUEST.value(), "Illegal Body by JSON Parse Fail", e.getMessage());
                }
            }
            return MetaData.build(traceId, HttpStatus.BAD_REQUEST.value(), "Message Not Readable", e.getMessage());
        }
        // Illegal Argument Type
        if (e instanceof MethodArgumentTypeMismatchException) {
            return MetaData.build(traceId, HttpStatus.BAD_REQUEST.value(), "Illegal Argument Type", e.getMessage());
        }
        // Method Not Allowed
        if (e instanceof HttpRequestMethodNotSupportedException) {
            return MetaData.build(traceId, HttpStatus.METHOD_NOT_ALLOWED.value(), "Method Not Allowed", null);
        }

        log.error("Internal Server Error", e);

        // SQL Exception
        if (e instanceof BadSqlGrammarException) {
            if (e.getCause() != null) {
                // SQL Syntax Error Exception
                if (e.getCause() instanceof SQLException) {
                    SQLException se = ((SQLException) e.getCause());
                    String stack = String.format("Bad SQL[Code:%s(State:%s)] %s", se.getErrorCode(), se.getSQLState(), se.getMessage());
                    return MetaData.build(traceId, HttpStatus.INTERNAL_SERVER_ERROR.value(), "Bad SQL Exception", stack);
                }
            }
            return MetaData.build(traceId, HttpStatus.INTERNAL_SERVER_ERROR.value(), "Unknown SQL Exception", e.getMessage());
        }

        // Unknown Internal Server Error
        return MetaData.build(traceId, HttpStatus.INTERNAL_SERVER_ERROR.value(), "Internal Server Error", e.getMessage());
    }

}