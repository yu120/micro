package cn.micro.biz.commons.enums;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.*;
import org.springframework.beans.BeanUtils;

import java.io.IOException;

/**
 * IEnum Json Deserializer
 *
 * @author lry
 */
public class IEnumJsonDeserializer extends JsonDeserializer<IEnum> {

    @SuppressWarnings("unchecked")
    @Override
    public IEnum deserialize(JsonParser jp, DeserializationContext dc) throws IOException, JsonProcessingException {
        String currentName = jp.currentName();
        Object currentValue = jp.getCurrentValue();
        Class findPropertyType = BeanUtils.findPropertyType(currentName, currentValue.getClass());

        JsonNode jsonNode = jp.getCodec().readTree(jp);
        if (!jsonNode.has(IEnum.VALUE_KEY)) {
            return IEnum.parse(findPropertyType, asValue(jsonNode));
        } else {
            return IEnum.parse(findPropertyType, asValue(jsonNode.get(IEnum.VALUE_KEY)));
        }
    }

    private Object asValue(JsonNode jsonNode) {
        if (jsonNode instanceof IntNode) {
            return jsonNode.asInt();
        } else if (jsonNode instanceof LongNode) {
            return jsonNode.asLong();
        } else if (jsonNode instanceof FloatNode) {
            return jsonNode.asDouble();
        } else if (jsonNode instanceof DoubleNode) {
            return jsonNode.asDouble();
        } else if (jsonNode instanceof NumericNode) {
            return jsonNode.asDouble();
        } else if (jsonNode instanceof BooleanNode) {
            return jsonNode.asBoolean();
        } else {
            return jsonNode.asText();
        }
    }

}


