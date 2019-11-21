package cn.micro.biz.commons.response;

import cn.micro.biz.commons.configuration.MicroProperties;
import cn.micro.biz.commons.configuration.MicroSpringUtils;
import cn.micro.biz.commons.exception.MicroStatusCode;
import com.alibaba.fastjson.JSON;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

import java.util.Arrays;
import java.util.HashSet;
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

    private Set<String> metaPackages = new HashSet<>();

    private final MicroProperties microProperties;

    @Override
    public void afterPropertiesSet() {
        metaPackages.addAll(MicroSpringUtils.BASE_PACKAGES);
        if (!(microProperties.getMetaPackages() == null || microProperties.getMetaPackages().length == 0)) {
            metaPackages.addAll(Arrays.stream(microProperties.getMetaPackages()).collect(Collectors.toSet()));
        }
        log.info("The meta packages: {}", metaPackages.toString());
    }

    @Override
    public boolean supports(MethodParameter methodParameter, Class<? extends HttpMessageConverter<?>> converterType) {
        return true;
    }

    @Override
    public Object beforeBodyWrite(Object obj,
                                  MethodParameter methodParameter,
                                  MediaType mediaType,
                                  Class<? extends HttpMessageConverter<?>> converterType,
                                  ServerHttpRequest serverHttpRequest,
                                  ServerHttpResponse serverHttpResponse) {
        if (methodParameter == null) {
            return obj;
        }

        String methodBeanClassName = methodParameter.getMember().getDeclaringClass().getName();
        if (metaPackages.stream().noneMatch(methodBeanClassName::startsWith)) {
            return obj;
        }

        // Non Bean and Method Annotation
        NonMetaData methodNonMetaData = methodParameter.getMethodAnnotation(NonMetaData.class);
        NonMetaData beanNonMetaData = methodParameter.getMember().getDeclaringClass().getAnnotation(NonMetaData.class);
        if (methodNonMetaData != null || beanNonMetaData != null || obj instanceof MetaData) {
            // don't need wrapper metaData data model
            return obj;
        }

        // need wrapper metaData data model
        if (converterType == null || !converterType.isAssignableFrom(StringHttpMessageConverter.class)) {
            return MicroStatusCode.buildSuccess(obj);
        }

        // return type is String
        return JSON.toJSONString(MicroStatusCode.buildSuccess(obj));
    }

}
