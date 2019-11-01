package cn.micro.biz.pubsrv.dingtalk.request;

import cn.micro.biz.pubsrv.dingtalk.AbstractOpenDingTalk;
import com.alibaba.fastjson.annotation.JSONField;
import lombok.*;

/**
 * 查询工作通知消息的发送结果请求
 *
 * @author lry
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class DingTalkMessageGetSendResultRequest extends DingTalkRequest {

    @JSONField(name = "agent_id")
    private Long agentId;
    @JSONField(name = "task_id")
    private Long taskId;

    public DingTalkMessageGetSendResultRequest() {
        super("https://oapi.dingtalk.com/topapi/message/corpconversation/getsendresult?access_token=%s", AbstractOpenDingTalk.HttpMethod.POST);
    }

}
