package cn.micro.biz.pubsrv.dingtalk.request;

import cn.micro.biz.pubsrv.dingtalk.message.DingTalkMsg;
import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.jsoup.Connection;

/**
 * 发送群消息请求
 *
 * @author lry
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class DingTalkChatSendRequest extends DingTalkRequest {

    @JSONField(name = "chatid")
    private String chatId;
    @JSONField(name = "msg")
    private DingTalkMsg msg;

    public DingTalkChatSendRequest() {
        super(Connection.Method.POST.name(),
                "https://oapi.dingtalk.com/chat/send?access_token=%s");
    }

}
