package cn.micro.biz.pubsrv.dingtalk.request;

import cn.micro.biz.pubsrv.dingtalk.AbstractOpenDingTalk;
import com.alibaba.fastjson.annotation.JSONField;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;

@Data
@AllArgsConstructor
public class DingTalkRequest implements Serializable {

    @JSONField(serialize = false, deserialize = false)
    private String url;
    @JSONField(serialize = false, deserialize = false)
    private AbstractOpenDingTalk.HttpMethod httpMethod;

}
