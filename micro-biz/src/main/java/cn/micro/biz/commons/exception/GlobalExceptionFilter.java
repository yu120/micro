package cn.micro.biz.commons.exception;

import cn.micro.biz.commons.auth.MicroAuthContext;
import cn.micro.biz.commons.configuration.MicroProperties;
import cn.micro.biz.commons.configuration.XssHttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.UUID;

/**
 * Exception Filter
 *
 * @author lry
 */
@Slf4j
@Order(0)
@Configuration
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class GlobalExceptionFilter extends OncePerRequestFilter {

    public static final String X_TRACE = "X-Trace";

    private final MicroProperties microProperties;

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain) throws IOException {
        if (request == null || response == null || filterChain == null) {
            return;
        }

        // 只要Header中有X-Trace,则自动解析
        String trace = request.getHeader(X_TRACE);
        if (trace == null || trace.length() == 0) {
            trace = request.getParameter(X_TRACE);
        }
        if (trace == null || trace.length() == 0) {
            trace = UUID.randomUUID().toString();
        }

        // 响应头统一返回请求ID
        request.setAttribute(X_TRACE, trace);
        response.setHeader(X_TRACE, trace);
        wrapperCORS(request, response);
        String ipAddress = MicroAuthContext.getRequestIPAddress();

        try {
            MDC.put(X_TRACE, trace);
            log.debug("Request enter: {}", ipAddress);
            filterChain.doFilter(new XssHttpServletRequest(request), response);
        } catch (Throwable t) {
            // write fail response
            response.setStatus(HttpStatus.OK.value());
            response.setCharacterEncoding(StandardCharsets.UTF_8.name());
            response.setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
            response.getWriter().print(MicroStatusCode.buildFailureJson(microProperties.isExceptionDebug(), t));
        } finally {
            log.debug("Request exist: {}", ipAddress);
            MDC.remove(X_TRACE);
        }
    }

    /**
     * 解决跨域
     *
     * @param response {@link HttpServletResponse}
     */
    private void wrapperCORS(HttpServletRequest request, HttpServletResponse response) {
        response.setHeader(HttpHeaders.ACCESS_CONTROL_ALLOW_ORIGIN, "*");
        if (HttpMethod.OPTIONS.name().equals(request.getMethod())) {
            response.setStatus(HttpStatus.NO_CONTENT.value());
            response.setHeader(HttpHeaders.ACCESS_CONTROL_MAX_AGE, "3600");
            response.setHeader(HttpHeaders.ACCESS_CONTROL_ALLOW_CREDENTIALS, "true");
            response.setHeader(HttpHeaders.ACCESS_CONTROL_ALLOW_METHODS, "POST, GET, OPTIONS, DELETE, PUT, HEADER");
            response.setHeader(HttpHeaders.ACCESS_CONTROL_ALLOW_HEADERS,
                    "Content-Type, X-Access-Token, X-Refresh-Token, X-Trace-Id, X-Requested-With");
        }
    }

}