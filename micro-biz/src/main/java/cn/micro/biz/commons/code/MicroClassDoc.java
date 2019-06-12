package cn.micro.biz.commons.code;

import lombok.Data;
import lombok.ToString;

import java.io.Serializable;
import java.util.LinkedHashMap;
import java.util.Map;

@Data
@ToString
public class MicroClassDoc implements Serializable {

    private String comment;
    private Map<String, MicroFieldDoc> fieldMap = new LinkedHashMap<>();

}
