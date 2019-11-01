package cn.micro.biz.pubsrv.dingtalk.request;

import cn.micro.biz.pubsrv.dingtalk.AbstractOpenDingTalk;
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

    private String appkey;
    private String appsecret;
    private String corpid;
    private String corpsecret;

}
