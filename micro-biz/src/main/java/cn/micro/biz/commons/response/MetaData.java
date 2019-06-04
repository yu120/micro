package cn.micro.biz.commons.response;

import cn.micro.biz.commons.configuration.MicroSpringConfiguration;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Response Meta Data Model
 *
 * @author lry
 */
@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class MetaData implements Serializable {

    private static final String TRACE_KEY = "trace";
    private static final String STACK_KEY = "stack";
    private static final String TIME_KEY = "time";
    private static final String SDF_PATTERN = "yyyy-MM-dd'T'HH:mm:ss.SSS";

    private int code;
    private String message;
    private Object data;
    private Map<String, Object> headers;

    public static MetaData build(Object traceId, int code, String message, Object data) {
        return build(traceId, code, message, data, null, new LinkedHashMap<>());
    }

    public static MetaData build(Object traceId, int code, String message, String stack) {
        return build(traceId, code, message, null, stack, new LinkedHashMap<>());
    }

    public static MetaData build(Object traceId, int code, String message, Object obj, String stack, Map<String, Object> headers) {
        if (headers == null) {
            headers = new LinkedHashMap<>();
        }
        headers.put(TRACE_KEY, traceId);
        headers.put(TIME_KEY, new SimpleDateFormat(SDF_PATTERN).format(new Date()));
        if (stack != null) {
            if (stack.length() > MicroSpringConfiguration.STACK_MAX_LENGTH) {
                headers.put(STACK_KEY, stack.substring(0, MicroSpringConfiguration.STACK_MAX_LENGTH));
            } else {
                headers.put(STACK_KEY, stack);
            }
        }

        return new MetaData(code, message, obj, headers);
    }

    public void setStack(String stack) {
        if (stack != null) {
            return;
        }

        if (headers == null) {
            headers = new LinkedHashMap<>();
        }
        headers.put(STACK_KEY, stack);
    }

}
