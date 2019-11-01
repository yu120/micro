package cn.micro.biz.pubsrv.dingtalk.request;

import cn.micro.biz.pubsrv.dingtalk.HttpMethod;
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
        super("https://oapi.dingtalk.com/gettoken?appkey=%s&appsecret=%s", HttpMethod.GET);
    }

    private String appkey;
    private String appsecret;
    private String corpid;
    private String corpsecret;

}
