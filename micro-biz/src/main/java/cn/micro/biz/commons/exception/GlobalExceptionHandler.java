package cn.micro.biz.commons.exception;

import cn.micro.biz.commons.configuration.MicroProperties;
import cn.micro.biz.commons.response.MetaData;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletRequest;

/**
 * Exception Filter
 *
 * @author lry
 */
@Slf4j
@RestControllerAdvice
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class GlobalExceptionHandler {

    private final MicroProperties microProperties;

    @ResponseBody
    @ExceptionHandler(value = Throwable.class)
    public MetaData defaultErrorHandler(HttpServletRequest request, Exception e) {
        Object traceId = request.getAttribute(GlobalExceptionFilter.X_TRACE_ID);
        boolean exceptionDebug = microProperties.isExceptionDebug();
        return MicroStatusCode.buildFailure(exceptionDebug, traceId, e);
    }

}