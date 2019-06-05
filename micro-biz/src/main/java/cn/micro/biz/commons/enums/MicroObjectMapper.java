package cn.micro.biz.commons.enums;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MicroObjectMapper extends ObjectMapper {

    public MicroObjectMapper() {
        super();
        SimpleModule module = new SimpleModule();
        module.addSerializer(IEnum.class, new IEnumJsonSerializer());
        module.addDeserializer(IEnum.class, new IEnumJsonDeserializer());
        super.registerModule(module);
    }

}
