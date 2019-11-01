package cn.micro.biz.pubsrv.dingtalk.request;

import cn.micro.biz.pubsrv.dingtalk.HttpMethod;
import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

import java.io.Serializable;

@Data
public class DingTalkRequest implements Serializable {

    @JSONField(serialize = false, deserialize = false)
    private HttpMethod httpMethod;

}
