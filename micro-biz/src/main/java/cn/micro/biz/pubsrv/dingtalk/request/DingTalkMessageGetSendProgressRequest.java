package cn.micro.biz.pubsrv.dingtalk.request;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.jsoup.Connection;

/**
 * 查询工作通知消息的发送进度请求
 *
 * @author lry
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class DingTalkMessageGetSendProgressRequest extends DingTalkRequest {

    @JSONField(name = "agent_id")
    private Long agentId;
    @JSONField(name = "task_id")
    private Long taskId;

    public DingTalkMessageGetSendProgressRequest() {
        super(Connection.Method.POST.name(),
                "https://oapi.dingtalk.com/topapi/message/corpconversation/getsendprogress?access_token=%s");
    }

}
