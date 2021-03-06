package cn.micro.biz.pubsrv.dingtalk.request;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.jsoup.Connection;

/**
 * 工作通知消息撤回请求
 *
 * @author lry
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class DingTalkMessageRecallRequest extends DingTalkRequest {

    @JSONField(name = "agent_id")
    private Long agentId;
    @JSONField(name = "msg_task_id")
    private Long msgTaskId;

    public DingTalkMessageRecallRequest() {
        super(Connection.Method.POST.name(),
                "https://oapi.dingtalk.com/topapi/message/corpconversation/recall?access_token=%s");
    }

}
