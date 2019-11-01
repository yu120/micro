package cn.micro.biz.pubsrv.wechat.request;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;

/**
 * WeChat Request
 *
 * @author lry
 */
@Data
@AllArgsConstructor
public class WeChatRequest implements Serializable {

    @JSONField(serialize = false, deserialize = false)
    private String method;
    @JSONField(serialize = false, deserialize = false)
    private String url;

}
