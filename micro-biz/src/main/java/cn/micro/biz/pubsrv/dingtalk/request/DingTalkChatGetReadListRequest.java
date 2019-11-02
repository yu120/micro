package cn.micro.biz.pubsrv.dingtalk.request;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.jsoup.Connection;

/**
 * 查询群消息已读人员列表请求
 *
 * @author lry
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class DingTalkChatGetReadListRequest extends DingTalkRequest {

    private Long cursor;
    private String messageId;
    private Long size;

    public DingTalkChatGetReadListRequest() {
        super(Connection.Method.GET.name(),
                "https://oapi.dingtalk.com/chat/getReadList?access_token=%s&messageId=%s&cursor=%s&size=%s");
    }

}
