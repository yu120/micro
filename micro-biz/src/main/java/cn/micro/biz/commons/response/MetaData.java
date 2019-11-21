package cn.micro.biz.commons.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * Response Meta Data Model
 *
 * @author lry
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MetaData implements Serializable {

    private int code;
    private String message;
    private Object data;
    private String stack;

    public MetaData(int code, String message, Object data) {
        this(code, message, data, null);
    }

}
