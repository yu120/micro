package cn.micro.biz.commons.enums;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;

/**
 * IEnum Json Serializer
 *
 * @author lry
 */
public class IEnumJsonSerializer extends JsonSerializer<IEnum> {

    @Override
    public void serialize(IEnum value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
        gen.writeStartObject();
        gen.writeObjectField(IEnum.VALUE_KEY, value.getValue());
        gen.writeStringField(IEnum.TITLE_KEY, value.getTitle());
        gen.writeEndObject();
    }

}
