package cn.micro.biz.pubsrv.wechat.request;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
public class WeChatRequest implements Serializable {

    @JSONField(serialize = false, deserialize = false)
    private String method;
    @JSONField(serialize = false, deserialize = false)
    private String url;

}
