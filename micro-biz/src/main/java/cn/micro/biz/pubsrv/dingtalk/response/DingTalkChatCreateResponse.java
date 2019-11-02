package cn.micro.biz.pubsrv.dingtalk.response;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 创建会话响应
 *
 * @author lry
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class DingTalkChatCreateResponse extends DingTalkResponse {

    @JSONField(name = "chatid")
    private String chatId;
    @JSONField(name = "conversationTag")
    private Long conversationTag;
    @JSONField(name = "openConversationId")
    private String openConversationId;

}
