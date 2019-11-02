package cn.micro.biz.pubsrv.dingtalk.request;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.jsoup.Connection;

import java.util.List;

/**
 * 修改会话请求
 *
 * @author lry
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class DingTalkChatUpdateRequest extends DingTalkRequest {

    @JSONField(name = "addExtidlist")
    private List<String> addExtIdList;
    @JSONField(name = "addUseridlist")
    private List<String> addUserIdList;
    private Long chatBannedType;
    @JSONField(name = "chatid")
    private String chatId;
    @JSONField(name = "delExtidlist")
    private List<String> delExtIdList;
    @JSONField(name = "delUseridlist")
    private List<String> delUserIdList;
    private String icon;
    private Boolean isBan;
    private Long managementType;
    private Long mentionAllAuthority;
    private String name;
    private String owner;
    private String ownerType;
    private Long searchable;
    private Long showHistoryType;
    private Long validationType;

    public DingTalkChatUpdateRequest() {
        super(Connection.Method.POST.name(),
                "https://oapi.dingtalk.com/chat/update?access_token=%s");
    }

}
