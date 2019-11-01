package cn.micro.biz.pubsrv.dingtalk.response;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * 查询工作通知消息的发送进度响应
 *
 * @author lry
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class DingTalkMessageGetSendProgressResponse extends DingTalkResponse {

    @JSONField(name = "progress")
    private AsyncSendProgress progress;

    public static class AsyncSendProgress implements Serializable {
        @JSONField(name = "progress_in_percent")
        private Long progressInPercent;
        @JSONField(name = "status")
        private Long status;
    }

}
