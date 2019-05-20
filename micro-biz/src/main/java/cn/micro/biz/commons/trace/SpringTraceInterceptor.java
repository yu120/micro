package cn.micro.biz.commons.trace;

import cn.micro.biz.commons.configuration.MicroSpringConfiguration;
import cn.micro.biz.commons.mybatis.extension.IMicroService;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import lombok.extern.slf4j.Slf4j;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.aop.Advisor;
import org.springframework.aop.aspectj.AspectJExpressionPointcut;
import org.springframework.aop.support.DefaultPointcutAdvisor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.PathMatcher;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Spring Trace Interceptor By {@link Aspect}
 *
 * @author lry
 */
@Slf4j
@Configuration
public class SpringTraceInterceptor implements MethodInterceptor {

    private static final PathMatcher PATH_MATCHER = new AntPathMatcher();
    private List<String> expressions = new ArrayList<>();

    @Resource
    private TraceProperties properties;

    @Override
    public Object invoke(MethodInvocation invocation) throws Throwable {
        // 排除不需要监控的URL
        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
        if (requestAttributes == null) {
            return invocation.proceed();
        } else {
            HttpServletRequest request = ((ServletRequestAttributes) requestAttributes).getRequest();
            for (String pattern : TraceStackContext.SKIP_URLS) {
                if (PATH_MATCHER.match(pattern, request.getServletPath())) {
                    return invocation.proceed();
                }
            }
        }

        String signature = invocation.getMethod().toString();
        Object[] argsPre = invocation.getArguments();
        List<Object> args = null;
        if (!(argsPre == null || argsPre.length == 0)) {
            args = Arrays.asList(argsPre);
        }

        String stack = null;
        Integer exception = null;
        Object ret = null;
        try {
            TraceStackContext.enter(signature, args);
            return ret = invocation.proceed();
        } catch (Throwable t) {
            stack = t.getClass().getSimpleName();
            exception = 0;
            throw t;
        } finally {
            TraceStackContext.exit(ret, exception, stack);
        }
    }

    /**
     * Create Aspect Pointcut by Spring Trace Interceptor
     *
     * @return {@link Advisor}
     */
    @Bean
    public Advisor traceAdvisor(MicroSpringConfiguration microSpringConfiguration) {
        if (properties.isDefaultExpressions()) {
            expressions.add("@annotation(" + Trace.class.getName() + ")");
            expressions.add("execution(* " + BaseMapper.class.getName() + ".*(..))");
            expressions.add("execution(* " + IMicroService.class.getName() + ".*(..))");

            Set<String> packagePrefixes = microSpringConfiguration.copyInstanceBasePackages();
            if (!(properties.getPackagePrefixes() == null || properties.getPackagePrefixes().length == 0)) {
                packagePrefixes.addAll(Arrays.stream(properties.getPackagePrefixes()).collect(Collectors.toSet()));
            }
            log.info("The package prefixes: {}", packagePrefixes.toString());

            for (String packagePrefix : packagePrefixes) {
                expressions.add("execution(* " + packagePrefix + ".web..*.*(..))");
                expressions.add("execution(* " + packagePrefix + ".mapper..*.*(..))");
                expressions.add("execution(* " + packagePrefix + ".service..*.*(..))");
                expressions.add("execution(* " + packagePrefix + ".controller..*.*(..))");
            }
        }

        List<String> tempExpressions = properties.getExpressions();
        if (!(tempExpressions == null || tempExpressions.isEmpty())) {
            expressions.addAll(tempExpressions);
        }

        StringBuilder sb = new StringBuilder();
        if (!(expressions == null || expressions.isEmpty())) {
            for (int i = 0; i < expressions.size(); i++) {
                if (i != 0) {
                    sb.append("||");
                }
                sb.append("(").append(expressions.get(i)).append(")");
            }
        }

        AspectJExpressionPointcut pointcut = new AspectJExpressionPointcut();
        pointcut.setExpression(sb.toString());
        log.info("The trace advisor expression: {}", pointcut.getExpression());
        return new DefaultPointcutAdvisor(pointcut, this);
    }

}
