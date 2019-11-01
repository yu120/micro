package cn.micro.biz.pubsrv.wechat.request;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;
import lombok.EqualsAndHashCode;


@Data
@EqualsAndHashCode(callSuper = true)
public class WeChatCode2SessionRequest extends WeChatRequest {

    /**
     * 小程序 appId
     */
    @JSONField(name = "appid")
    private String appId;
    /**
     * 小程序 appSecret
     */
    @JSONField(name = "secret")
    private String secret;
    /**
     * 登录时获取的 code
     * <p>
     * 临时登录凭证 code 只能使用一次
     */
    @JSONField(name = "js_code")
    private String jsCode;
    /**
     * 授权类型，此处只需填写 authorization_code
     */
    @JSONField(name = "grant_type")
    private String grantType = "authorization_code";

    public WeChatCode2SessionRequest() {
        super("GET", "https://api.weixin.qq.com/sns/jscode2session?appid=%s&secret=%s&js_code=%s&grant_type=authorization_code");
    }

}
