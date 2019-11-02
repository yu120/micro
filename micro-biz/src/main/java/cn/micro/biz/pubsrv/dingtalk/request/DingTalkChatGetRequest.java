package cn.micro.biz.pubsrv.dingtalk.request;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.jsoup.Connection;

/**
 * 获取会话请求
 *
 * @author lry
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class DingTalkChatGetRequest extends DingTalkRequest {

    @JSONField(name = "chatid")
    private String chatId;

    public DingTalkChatGetRequest() {
        super(Connection.Method.GET.name(),
                "https://oapi.dingtalk.com/chat/get?access_token=%s&chatid=%s");
    }

}
