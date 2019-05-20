package cn.micro.biz.commons.response;

import cn.micro.biz.commons.configuration.MicroProperties;
import cn.micro.biz.commons.configuration.MicroSpringConfiguration;
import cn.micro.biz.commons.exception.GlobalExceptionFilter;
import com.alibaba.fastjson.JSON;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

import javax.annotation.Nullable;
import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Global Response Body Advice
 *
 * @author lry
 */
@Slf4j
@RestControllerAdvice
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class GlobalResponseBodyAdvice implements InitializingBean, ResponseBodyAdvice<Object> {

    private Set<String> metaPackages;

    private final MicroProperties microProperties;

    @Override
    public void afterPropertiesSet() {
        metaPackages = MicroSpringConfiguration.copyBasePackages();
        if (!(microProperties.getMetaPackages() == null || microProperties.getMetaPackages().length == 0)) {
            metaPackages.addAll(Arrays.stream(microProperties.getMetaPackages()).collect(Collectors.toSet()));
        }
        log.info("The meta packages: {}", metaPackages.toString());
    }

    @Override
    public boolean supports(@Nullable MethodParameter methodParameter,
                            @Nullable Class<? extends HttpMessageConverter<?>> converterType) {
        return true;
    }

    @Override
    public Object beforeBodyWrite(Object obj,
                                  @Nullable MethodParameter methodParameter,
                                  @Nullable MediaType mediaType,
                                  @Nullable Class<? extends HttpMessageConverter<?>> converterType,
                                  @Nullable ServerHttpRequest serverHttpRequest,
                                  @Nullable ServerHttpResponse serverHttpResponse) {
        Object traceId = MDC.get(GlobalExceptionFilter.X_TRACE_ID);
        if (methodParameter == null) {
            return obj;
        }

        String methodBeanClassName = methodParameter.getMember().getDeclaringClass().getName();
        if (metaPackages.stream().noneMatch(methodBeanClassName::startsWith)) {
            return obj;
        }

        // Non Bean and Method Annotation
        NonMeta methodNonMeta = methodParameter.getMethodAnnotation(NonMeta.class);
        NonMeta beanNonMeta = methodParameter.getMember().getDeclaringClass().getAnnotation(NonMeta.class);
        if (methodNonMeta != null || beanNonMeta != null || obj instanceof MetaData) {
            // don't need wrapper metaData data model
            return obj;
        }

        // need wrapper metaData data model
        if (converterType == null || !converterType.isAssignableFrom(StringHttpMessageConverter.class)) {
            return MetaData.build(traceId, HttpStatus.OK.value(), HttpStatus.OK.getReasonPhrase(), obj);
        }

        // return type is String
        MetaData metaData = MetaData.build(traceId, HttpStatus.OK.value(), HttpStatus.OK.getReasonPhrase(), obj);
        return JSON.toJSONString(metaData);
    }

}
