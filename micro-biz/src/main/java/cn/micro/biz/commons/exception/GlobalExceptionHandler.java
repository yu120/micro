package cn.micro.biz.commons.exception;

import cn.micro.biz.commons.configuration.MicroProperties;
import cn.micro.biz.commons.response.MetaData;
import cn.micro.biz.pubsrv.event.ExceptionEventAlarm;
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
    ExceptionEventAlarm exceptionEventAlarm = new ExceptionEventAlarm(
            "SECe6439e9c8d5ddde21cc271f6c83f205f635bd8cb63bb2af893b6019a93d4ef80",
            "cf0cd4f757a3c5e0cba3e05ddd7edbe5212be0b14ad9ecf5990a934b83cd84c0");

    @ResponseBody
    @ExceptionHandler(value = Throwable.class)
    public MetaData defaultErrorHandler(HttpServletRequest request, Exception e) {
        exceptionEventAlarm.eventCollect(e);
        Object traceId = request.getAttribute(GlobalExceptionFilter.X_TRACE_ID);
        boolean exceptionDebug = microProperties.isExceptionDebug();
        return MicroStatusCode.buildFailure(exceptionDebug, traceId, e);
    }

}