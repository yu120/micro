package cn.micro.biz.commons.code;

import lombok.Data;
import lombok.ToString;

import java.io.Serializable;

@Data
@ToString
public class MicroFieldDoc implements Serializable {

    private String fieldName;
    private String comment;
    private String serial;
    private String serialField;

}
