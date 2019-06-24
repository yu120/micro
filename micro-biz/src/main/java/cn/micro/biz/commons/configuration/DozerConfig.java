package cn.micro.biz.commons.configuration;

import com.github.dozermapper.core.DozerBeanMapperBuilder;
import com.github.dozermapper.core.Mapper;
import com.github.dozermapper.core.loader.api.BeanMappingBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DozerConfig {

    @Bean
    public Mapper dozerMapper(){
        Mapper mapper = DozerBeanMapperBuilder.create()
                //指定 mapping 的配置文件(放到 resources 类路径下即可)，可添加多个 xml 文件，用逗号隔开
                //.withMappingFiles("dozerBeanMapping.xml")
                .withMappingBuilder(beanMappingBuilder())
                .withEventListener(new GlobalDozerEventListener())
                .build();
        return mapper;
    }

    @Bean
    public BeanMappingBuilder beanMappingBuilder() {
        return new BeanMappingBuilder() {
            @Override
            protected void configure() {
                // 个性化配置添加在此
            }
        };
    }

}
