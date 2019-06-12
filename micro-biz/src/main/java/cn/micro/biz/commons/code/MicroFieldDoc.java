package cn.micro.biz.commons.code;

import lombok.Data;
import lombok.ToString;

import java.io.Serializable;
import java.util.Map;

@Data
@ToString
public class MicroFieldDoc implements Serializable {

    private String fieldName;
    private String serialField;
    private String comment;

}
