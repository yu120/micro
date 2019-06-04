package cn.micro.biz.commons.exception;

import cn.micro.biz.commons.configuration.MicroProperties;
import cn.micro.biz.commons.response.MetaData;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

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

        Object traceId = request.getAttribute(GlobalExceptionFilter.X_TRACE_ID);
        MetaData metaData = MicroStatus.buildMetaData(traceId, e);

        // print internal server error
        if (metaData.getCode() >= HttpStatus.INTERNAL_SERVER_ERROR.value() && !microProperties.isShowAllError()) {
            log.error("Internal Server Error", e);
        }

        return metaData;
    }

}