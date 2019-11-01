package cn.micro.biz.pubsrv.dingtalk.request;

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

    public static final String URL = "https://oapi.dingtalk.com/gettoken?appkey=%s&appsecret=%s";

    private String appkey;
    private String appsecret;
    private String corpid;
    private String corpsecret;

}
