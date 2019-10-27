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

    private String title;
    private String desc;

    private String className;
    private String methodName;

    private List<Object> args;
    private Object result;

}
