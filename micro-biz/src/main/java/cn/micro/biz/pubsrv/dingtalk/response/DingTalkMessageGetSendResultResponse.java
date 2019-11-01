package cn.micro.biz.pubsrv.dingtalk.response;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * 查询工作通知消息的发送结果响应
 *
 * @author lry
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class DingTalkMessageGetSendResultResponse extends DingTalkResponse {

    @JSONField(name = "send_result")
    private AsyncSendResult sendResult;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class AsyncSendResult implements Serializable {
        @JSONField(name = "failed_user_id_list")
        private List<String> failedUserIdList;
        @JSONField(name = "forbidden_user_id_list")
        private List<String> forbiddenUserIdList;
        @JSONField(name = "invalid_dept_id_list")
        private List<Long> invalidDeptIdList;
        @JSONField(name = "invalid_user_id_list")
        private List<String> invalidUserIdList;
        @JSONField(name = "read_user_id_list")
        private List<String> readUserIdList;
        @JSONField(name = "unread_user_id_list")
        private List<String> unreadUserIdList;
    }

}
