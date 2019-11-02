package cn.micro.biz.pubsrv.dingtalk.request;

import cn.micro.biz.pubsrv.dingtalk.message.DingTalkMsg;
import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.jsoup.Connection;

/**
 * 发送普通消息请求
 *
 * @author lry
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class DingTalkMessageSendRequest extends DingTalkRequest {

    private String cid;
    private String sender;
    @JSONField(name = "msg")
    private DingTalkMsg msg;

    public DingTalkMessageSendRequest() {
        super(Connection.Method.POST.name(),
                "https://oapi.dingtalk.com/message/send_to_conversation?access_token=%s");
    }

}
