package cn.micro.biz.pubsrv.event;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * Event Collect
 *
 * @author lry
 */
@Data
public class EventCollect implements Serializable {

    /**
     * The event collect title
     */
    private String title;
    /**
     * The event collect describe
     */
    private String desc;

    /**
     * The event collect class name
     */
    private String className;
    /**
     * The event collect method name
     */
    private String methodName;

    /**
     * The event collect arg list
     */
    private List<Object> args;
    /**
     * The event collect response result
     */
    private Object result;

}
