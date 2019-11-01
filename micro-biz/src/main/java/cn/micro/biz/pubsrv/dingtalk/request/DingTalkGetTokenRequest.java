package cn.micro.biz.pubsrv.dingtalk.request;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.jsoup.Connection;

/**
 * 获取access_token请求
 *
 * @author lry
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class DingTalkGetTokenRequest extends DingTalkRequest {

    public DingTalkGetTokenRequest() {
        super(Connection.Method.GET.name(),
                "https://oapi.dingtalk.com/gettoken?appkey=%s&appsecret=%s");
    }

    @JSONField(name = "appkey")
    private String appKey;
    @JSONField(name = "appsecret")
    private String appSecret;
    @JSONField(name = "corpid")
    private String corpId;
    @JSONField(name = "corpsecret")
    private String corpSecret;

}
