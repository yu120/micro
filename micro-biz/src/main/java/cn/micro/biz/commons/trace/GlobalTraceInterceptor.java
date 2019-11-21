package cn.micro.biz.commons.trace;

import cn.micro.biz.commons.exception.GlobalExceptionFilter;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.PathMatcher;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.concurrent.TimeUnit;

/**
 * The Trace Interceptor By {@link HandlerInterceptor}
 *
 * @author lry
 */
@Slf4j
@Configuration
@EnableConfigurationProperties(TraceProperties.class)
@ConditionalOnProperty(prefix = "micro.trace", name = "enable", havingValue = "true")
public class GlobalTraceInterceptor implements InitializingBean, DisposableBean, WebMvcConfigurer, HandlerInterceptor {

    private static final PathMatcher PATH_MATCHER = new AntPathMatcher();

    @Getter
    private static Cache<String, String> cache;

    @Resource
    private TraceProperties properties;

    @Override
    public void afterPropertiesSet() {
        if (properties.isCacheEnable()) {
            cache = CacheBuilder.newBuilder()
                    .maximumSize(properties.getCacheMaximumSize())
                    .expireAfterWrite(properties.getCacheExpireAfterWrite().getSeconds(), TimeUnit.SECONDS).build();
        }

        TraceStackContext.initialize(properties);
        TraceStatistic.INSTANCE.initialize(properties);
        log.info("[Initialize Global Trace Interceptor]: {}", this.getClass().getName());
    }

    @Override
    public void destroy() {
        TraceStatistic.INSTANCE.destroy();
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(this);
    }

    @Override
    public boolean preHandle(HttpServletRequest request,
                             HttpServletResponse response, Object handler) throws Exception {
        if (!(handler instanceof HandlerMethod)) {
            return true;
        }

        // 排除不需要监控的URL
        for (String pattern : TraceStackContext.SKIP_URLS) {
            if (PATH_MATCHER.match(pattern, request.getServletPath())) {
                return true;
            }
        }

        String url = request.getRequestURI();
        String queryString = request.getQueryString();
        if (StringUtils.isNotBlank(queryString)) {
            url += "?" + queryString;
        }

        TraceStackContext.start(request.getMethod() + " " + url);
        TraceStackContext.enter("pre-request", null);
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response,
                           Object handler, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response,
                                Object handler, Exception ex) throws Exception {
        if (!(handler instanceof HandlerMethod)) {
            return;
        }

        // 排除不需要转换的URL
        for (String pattern : TraceStackContext.SKIP_URLS) {
            if (PATH_MATCHER.match(pattern, request.getServletPath())) {
                return;
            }
        }

        TraceStackContext.exit(null, null, null);
        Pair<String, Long> duration = TraceStackContext.stopAndPrint();
        if (duration != null) {
            if (cache != null) {
                String requestId = response.getHeader(GlobalExceptionFilter.X_TRACE);
                if (requestId != null && requestId.length() > 0) {
                    cache.put(requestId, duration.getKey());
                }
            }

            // 统计响应时长分布
            TraceStatistic.INSTANCE.put(this.getPatternRequestURI(request, handler), duration.getRight().intValue());
        }
    }

    private String getPatternRequestURI(HttpServletRequest request, Object handler) {
        return request.getMethod() + "@" + request.getRequestURI();
    }

}
