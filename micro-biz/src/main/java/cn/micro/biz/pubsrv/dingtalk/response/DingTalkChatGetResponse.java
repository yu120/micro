package cn.micro.biz.pubsrv.dingtalk.response;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.List;

/**
 * 获取会话响应
 *
 * @author lry
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class DingTalkChatGetResponse extends DingTalkResponse {

    @JSONField(name = "chat_info")
    private ChatInfo chatInfo;

    @Data
    public static class ChatInfo implements Serializable {
        @JSONField(name = "agentidlist")
        private List<String> agentIdList;
        @JSONField(name = "chatBannedType")
        private Long chatBannedType;
        @JSONField(name = "conversationTag")
        private Long conversationTag;
        @JSONField(name = "extidlist")
        private List<String> extIdList;
        @JSONField(name = "icon")
        private String icon;
        @JSONField(name = "managementType")
        private Long managementType;
        @JSONField(name = "mentionAllAuthority")
        private Long mentionAllAuthority;
        @JSONField(name = "name")
        private String name;
        @JSONField(name = "owner")
        private String owner;
        @JSONField(name = "searchable")
        private Long searchable;
        @JSONField(name = "showHistoryType")
        private Long showHistoryType;
        @JSONField(name = "useridlist")
        private List<String> userIdList;
        @JSONField(name = "validationType")
        private Long validationType;
    }

}
