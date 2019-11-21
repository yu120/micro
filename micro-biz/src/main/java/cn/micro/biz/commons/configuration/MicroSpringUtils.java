package cn.micro.biz.commons.configuration;

import cn.micro.biz.commons.auth.GlobalAuthHandlerInterceptor;
import com.alibaba.fastjson.support.spring.FastJsonHttpMessageConverter;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigurationPackages;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Micro Spring Utils
 *
 * @author lry
 */
@Slf4j
@Getter
@Configuration
@EnableConfigurationProperties(MicroProperties.class)
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class MicroSpringUtils implements ApplicationContextAware, InitializingBean, WebMvcConfigurer, DisposableBean {

    public static List<String> BASE_PACKAGES = new ArrayList<>();
    public static ApplicationContext APPLICATION_CONTEXT;
    public static Map<String, MicroRequestMappingInfo> REQUEST_MAPPING_INFO_MAP = new ConcurrentHashMap<>();

    private RequestMappingHandlerMapping requestMappingHandlerMapping;

    private final GlobalAuthHandlerInterceptor globalAuthHandlerInterceptor;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        if (applicationContext != null) {
            APPLICATION_CONTEXT = applicationContext;
            BASE_PACKAGES.addAll(AutoConfigurationPackages.get(applicationContext));
        }
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        requestMappingHandlerMapping = APPLICATION_CONTEXT.getBean(RequestMappingHandlerMapping.class);
        Map<RequestMappingInfo, HandlerMethod> mapRet = requestMappingHandlerMapping.getHandlerMethods();
        for (Map.Entry<RequestMappingInfo, HandlerMethod> entry : mapRet.entrySet()) {
            RequestMappingInfo requestMappingInfo = entry.getKey();
            HandlerMethod handlerMethod = entry.getValue();
            Set<String> patterns = requestMappingInfo.getPatternsCondition().getPatterns();
            for (String pattern : patterns) {
                Set<RequestMethod> requestMethodSet = requestMappingInfo.getMethodsCondition().getMethods();
                for (RequestMethod requestMethod : requestMethodSet) {
                    String uniqueId = requestMethod.name() + "@" + pattern;
                    REQUEST_MAPPING_INFO_MAP.put(uniqueId, new MicroRequestMappingInfo(uniqueId, requestMappingInfo, handlerMethod));
                }
            }
        }
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(globalAuthHandlerInterceptor);
    }

    @Override
    public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
        converters.add(new FastJsonHttpMessageConverter());
    }

    public static ObjectMapper getObjectMapper() {
        return APPLICATION_CONTEXT.getBean(MappingJackson2HttpMessageConverter.class).getObjectMapper();
    }

    @Override
    public void destroy() throws Exception {

    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class MicroRequestMappingInfo implements Serializable {
        private String uniqueId;
        private RequestMappingInfo requestMappingInfo;
        private HandlerMethod handlerMethod;
    }

}
