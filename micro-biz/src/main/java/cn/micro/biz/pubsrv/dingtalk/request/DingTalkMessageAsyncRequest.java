package cn.micro.biz.pubsrv.dingtalk.request;

import cn.micro.biz.pubsrv.dingtalk.AbstractOpenDingTalk;
import cn.micro.biz.pubsrv.dingtalk.message.DingTalkMsg;
import com.alibaba.fastjson.annotation.JSONField;
import lombok.*;

import java.util.List;

/**
 * 异步发送工作通知消息请求
 *
 * @author lry
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class DingTalkMessageAsyncRequest extends DingTalkRequest {

    @JSONField(name = "agent_id")
    private Long agentId;
    @JSONField(name = "dept_id_list")
    private String deptIdList;
    @JSONField(name = "msg")
    private DingTalkMsg msg;
    @JSONField(name = "to_all_user")
    private Boolean toAllUser;
    @JSONField(name = "userid_list")
    private String useridList;

    public DingTalkMessageAsyncRequest() {
        super(AbstractOpenDingTalk.HttpMethod.POST,
                "https://oapi.dingtalk.com/topapi/message/corpconversation/asyncsend_v2?access_token=%s");
    }

    public void setDeptIdList(List<String> deptIdList) {
        this.deptIdList = String.join(",", deptIdList.toArray(new String[0]));
    }

    public void setUserIdList(List<String> userIdList) {
        this.useridList = String.join(",", userIdList.toArray(new String[0]));
    }

}
