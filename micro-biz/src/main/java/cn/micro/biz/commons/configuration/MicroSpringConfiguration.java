package cn.micro.biz.commons.configuration;

import cn.micro.biz.commons.auth.GlobalAuthHandlerInterceptor;
import com.alibaba.fastjson.support.spring.FastJsonHttpMessageConverter;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigurationPackages;
import org.springframework.boot.autoconfigure.web.servlet.WebMvcRegistrations;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import javax.annotation.Nullable;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Micro Spring Configuration
 *
 * @author lry
 */
@Slf4j
@Configuration
@EnableConfigurationProperties(MicroProperties.class)
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class MicroSpringConfiguration implements ApplicationContextAware, WebMvcConfigurer {

    @Getter
    private static ApplicationContext applicationContext;
    @Getter
    private static List<String> basePackages;

    private List<String> instanceBasePackages;

    private final MicroProperties microProperties;
    private final GlobalAuthHandlerInterceptor globalAuthHandlerInterceptor;

    @Bean
    public WebMvcRegistrations customWebMvcRegistrations() {
        return new WebMvcRegistrations() {
            @Override
            public RequestMappingHandlerMapping getRequestMappingHandlerMapping() {
                return new MicroRequestMappingHandlerMapping();
            }
        };
    }

    @Override
    public void setApplicationContext(@Nullable ApplicationContext applicationContext) throws BeansException {
        if (applicationContext != null) {
            MicroSpringConfiguration.applicationContext = applicationContext;
            this.instanceBasePackages = AutoConfigurationPackages.get(applicationContext);
            MicroSpringConfiguration.basePackages = instanceBasePackages;
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

    public static Set<String> copyBasePackages() {
        Set<String> tempBasePackages = new HashSet<>();
        if (basePackages != null && !basePackages.isEmpty()) {
            tempBasePackages.addAll(basePackages);
        }

        return tempBasePackages;
    }

    public Set<String> copyInstanceBasePackages() {
        Set<String> basePackages = new HashSet<>();
        if (instanceBasePackages == null || instanceBasePackages.isEmpty()) {
            return basePackages;
        }

        basePackages.addAll(instanceBasePackages);
        return basePackages;
    }


}
