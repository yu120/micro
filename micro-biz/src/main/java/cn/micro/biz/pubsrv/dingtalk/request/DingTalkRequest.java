package cn.micro.biz.pubsrv.dingtalk.request;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;

@Data
@AllArgsConstructor
public class DingTalkRequest implements Serializable {

    @JSONField(serialize = false, deserialize = false)
    private String method;
    @JSONField(serialize = false, deserialize = false)
    private String url;

}
