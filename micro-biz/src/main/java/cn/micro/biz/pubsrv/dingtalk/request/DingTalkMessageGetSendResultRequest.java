package cn.micro.biz.pubsrv.dingtalk.request;

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

    public static final String URL = "https://oapi.dingtalk.com/topapi/message/corpconversation/getsendresult?access_token=%s";

    @JSONField(name = "agent_id")
    private Long agentId;
    @JSONField(name = "task_id")
    private Long taskId;

}
