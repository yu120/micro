package cn.micro.biz.pubsrv.dingtalk.response;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 异步发送工作通知消息响应
 *
 * @author lry
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class DingTalkMessageAsyncResponse extends DingTalkResponse {

    @JSONField(name = "task_id")
    private Long taskId;

}
