package cn.micro.biz.pubsrv.im.notify;

import lombok.Data;
import lombok.ToString;
import org.springframework.web.context.request.RequestAttributes;

import java.io.Serializable;
import java.util.Map;
import java.util.UUID;

/**
 * YunXin Notify Event
 *
 * @author lry
 */
@Data
@ToString
public class YunXinNotifyEvent implements Serializable {

    private String id = UUID.randomUUID().toString();
    private String memberId;
    private Map<String, String> contextMap;
    private RequestAttributes requestAttributes;

}