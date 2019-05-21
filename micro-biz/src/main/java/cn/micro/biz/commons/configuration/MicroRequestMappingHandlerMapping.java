package cn.micro.biz.commons.configuration;

import org.apache.commons.lang3.StringUtils;
import org.springframework.core.annotation.AnnotatedElementUtils;
import org.springframework.web.servlet.mvc.condition.RequestCondition;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import java.lang.reflect.Method;

public class MicroRequestMappingHandlerMapping extends RequestMappingHandlerMapping {

    @Override
    protected RequestCondition<?> getCustomTypeCondition(Class<?> handlerType) {
        return super.getCustomTypeCondition(handlerType);
    }

    private RequestMappingInfo getApiVersionMappingInfo(Method method, Class<?> handlerType) {
        ApiVersion apiVersion = AnnotatedElementUtils.findMergedAnnotation(method, ApiVersion.class);
        if (apiVersion == null || StringUtils.isBlank(apiVersion.value())) {
            apiVersion = AnnotatedElementUtils.findMergedAnnotation(handlerType, ApiVersion.class);
        }
        return apiVersion == null || StringUtils.isBlank(apiVersion.value()) ? null : RequestMappingInfo
                .paths(apiVersion.value())
                .build();
    }

}
