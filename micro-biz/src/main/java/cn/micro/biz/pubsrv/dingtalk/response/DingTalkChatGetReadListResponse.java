package cn.micro.biz.pubsrv.dingtalk.response;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * 查询群消息已读人员列表响应
 *
 * @author lry
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class DingTalkChatGetReadListResponse extends DingTalkResponse {

    @JSONField(name = "next_cursor")
    private Long nextCursor;
    @JSONField(name = "readUserIdList")
    private List<String> readUserIdList;

}
