package cn.micro.biz.pubsrv.wechat;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class WeChatRequest implements Serializable {

    @JSONField(serialize = false, deserialize = false)
    private String method;
    @JSONField(serialize = false, deserialize = false)
    private String url;

}
