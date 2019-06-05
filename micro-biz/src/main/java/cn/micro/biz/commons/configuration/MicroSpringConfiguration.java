package cn.micro.biz.commons.configuration;

import cn.micro.biz.commons.auth.GlobalAuthHandlerInterceptor;
import cn.micro.biz.commons.enums.IEnum;
import cn.micro.biz.commons.enums.IEnumJsonDeserializer;
import cn.micro.biz.commons.enums.IEnumJsonSerializer;
import com.alibaba.fastjson.support.spring.FastJsonHttpMessageConverter;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigurationPackages;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

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
public class MicroSpringConfiguration implements ApplicationContextAware, InitializingBean, WebMvcConfigurer {

    public static int STACK_MAX_LENGTH;
    private static ApplicationContext APPLICATION_CONTEXT;
    private static List<String> BASE_PACKAGES;

    private final MicroProperties microProperties;
    private final GlobalAuthHandlerInterceptor globalAuthHandlerInterceptor;
    private final MappingJackson2HttpMessageConverter mappingJackson2HttpMessageConverter;

    public static ObjectMapper getObjectMapper() {
        return APPLICATION_CONTEXT.getBean(MappingJackson2HttpMessageConverter.class).getObjectMapper();
    }

    @Override
    public void setApplicationContext(@Nullable ApplicationContext applicationContext) throws BeansException {
        if (applicationContext != null) {
            MicroSpringConfiguration.APPLICATION_CONTEXT = applicationContext;
            MicroSpringConfiguration.BASE_PACKAGES = AutoConfigurationPackages.get(applicationContext);

            // set enum serializer/deserializer object mapper
            SimpleModule module = new SimpleModule();
            module.addSerializer(IEnum.class, new IEnumJsonSerializer());
            module.addDeserializer(IEnum.class, new IEnumJsonDeserializer());
            ObjectMapper objectMapper = applicationContext.getBean(ObjectMapper.class);
            objectMapper.registerModule(module);
        }
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        MicroSpringConfiguration.STACK_MAX_LENGTH = microProperties.getStackMaxLength();
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
        if (BASE_PACKAGES != null && !BASE_PACKAGES.isEmpty()) {
            tempBasePackages.addAll(BASE_PACKAGES);
        }

        return tempBasePackages;
    }

    public Set<String> copyInstanceBasePackages() {
        Set<String> basePackages = new HashSet<>();
        if (BASE_PACKAGES == null || BASE_PACKAGES.isEmpty()) {
            return basePackages;
        }

        basePackages.addAll(BASE_PACKAGES);
        return basePackages;
    }

}
