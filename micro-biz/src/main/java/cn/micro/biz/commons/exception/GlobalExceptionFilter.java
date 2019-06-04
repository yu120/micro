package cn.micro.biz.commons.exception;

import cn.micro.biz.commons.configuration.MicroProperties;
import cn.micro.biz.commons.configuration.SpringOrder;
import cn.micro.biz.commons.utils.IPUtils;
import cn.micro.biz.commons.utils.IdGenerator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.annotation.Nullable;
import javax.servlet.FilterChain;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

/**
 * Exception Filter
 *
 * @author lry
 */
@Slf4j
@Configuration
@Order(SpringOrder.GLOBAL_EXCEPTION)
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class GlobalExceptionFilter extends OncePerRequestFilter {

    public static final String X_TRACE_ID = "X-Trace-Id";
    private static final IdGenerator ID_GENERATOR = new IdGenerator(0, 0);

    private final MicroProperties microProperties;

    @Override
    protected void doFilterInternal(
            @Nullable HttpServletRequest request,
            @Nullable HttpServletResponse response,
            @Nullable FilterChain filterChain) throws IOException {
        if (request == null || response == null || filterChain == null) {
            return;
        }

        // 只要Header中有X-Trace-Id,则自动解析
        String traceId = request.getHeader(X_TRACE_ID);
        if (traceId == null || traceId.length() == 0) {
            traceId = request.getParameter(X_TRACE_ID);
        }
        if (traceId == null || traceId.length() == 0) {
            traceId = String.valueOf(ID_GENERATOR.nextId());
        }

        // 响应头统一返回请求ID
        request.setAttribute(X_TRACE_ID, traceId);
        response.setHeader(X_TRACE_ID, traceId);
        wrapperCORS(response);
        String ipAddress = IPUtils.getRequestIPAddress(request);

        try {
            MDC.put(X_TRACE_ID, traceId);
            log.debug("Request enter: {}", ipAddress);
            filterChain.doFilter(request, response);
        } catch (Throwable t) {
            // write fail response
            response.setStatus(HttpStatus.OK.value());
            response.setCharacterEncoding(StandardCharsets.UTF_8.name());
            response.setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
            response.getWriter().print(MicroStatus.buildFailureJSON(microProperties.isExceptionDebug(), traceId, t));
        } finally {
            log.debug("Request exist: {}", ipAddress);
            MDC.remove(X_TRACE_ID);
        }
    }

    /**
     * 解决跨域
     *
     * @param response {@link HttpServletResponse}
     */
    private void wrapperCORS(HttpServletResponse response) {
        response.setHeader(HttpHeaders.ACCESS_CONTROL_MAX_AGE, "3600");
        response.setHeader(HttpHeaders.ACCESS_CONTROL_ALLOW_ORIGIN, "*");
        response.setHeader(HttpHeaders.ACCESS_CONTROL_ALLOW_CREDENTIALS, "true");
        response.setHeader(HttpHeaders.ACCESS_CONTROL_ALLOW_METHODS, "POST, GET, OPTIONS, DELETE, PUT, HEADER");
        response.setHeader(HttpHeaders.ACCESS_CONTROL_ALLOW_HEADERS,
                "Content-Type, X-Access-Token, X-Refresh-Token, X-Trace-Id, X-Requested-With");
    }

}