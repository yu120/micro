package cn.micro.biz.pubsrv.dingtalk.request;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.jsoup.Connection;

import java.util.List;

/**
 * 创建会话请求
 *
 * @author lry
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class DingTalkChatCreateRequest extends DingTalkRequest {

    private Long chatBannedType;
    private Long conversationTag;
    @JSONField(name = "extidlist")
    private List<String> extIdList;
    private String icon;
    private Long managementType;
    private Long mentionAllAuthority;
    private String name;
    private String owner;
    private String ownerType;
    private Long searchable;
    private Long showHistoryType;
    @JSONField(name = "useridlist")
    private List<String> userIdList;
    private Long validationType;

    public DingTalkChatCreateRequest() {
        super(Connection.Method.POST.name(),
                "https://oapi.dingtalk.com/chat/create?access_token=%s");
    }

}
