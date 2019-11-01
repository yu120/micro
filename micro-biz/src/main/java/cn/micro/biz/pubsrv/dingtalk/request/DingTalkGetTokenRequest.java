package cn.micro.biz.pubsrv.dingtalk.request;

import cn.micro.biz.pubsrv.dingtalk.AbstractOpenDingTalk;
import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 获取access_token请求
 *
 * @author lry
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class DingTalkGetTokenRequest extends DingTalkRequest {

    public DingTalkGetTokenRequest() {
        super(AbstractOpenDingTalk.HttpMethod.GET,
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
