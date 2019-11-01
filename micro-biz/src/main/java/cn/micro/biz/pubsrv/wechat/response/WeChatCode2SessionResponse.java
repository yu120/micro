package cn.micro.biz.pubsrv.wechat.response;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * WeChat Code2Session Response
 *
 * @author lry
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class WeChatCode2SessionResponse extends WeChatResponse {

    /**
     * 用户唯一标识
     */
    @JSONField(name = "openid")
    private String openId;
    /**
     * 会话密钥
     * <p>
     * 会话密钥 session_key 是对用户数据进行 加密签名 的密钥。为了应用自身的数据安全，
     * 开发者服务器不应该把会话密钥下发到小程序，也不应该对外提供这个密钥。
     */
    @JSONField(name = "session_key")
    private String sessionKey;
    /**
     * 用户在开放平台的唯一标识符，在满足 UnionID 下发条件的情况下会返回
     * <p>
     * 同一个微信开放平台帐号下的移动应用、网站应用和公众帐号（包括小程序），用户的 UnionID 是唯一的
     */
    @JSONField(name = "unionid")
    private String unionId;

}
