package cn.micro.biz.pubsrv.wx;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;
import lombok.ToString;

import java.io.Serializable;

@Data
@ToString
public class WxAuthCode2Session implements Serializable {

    /**
     * 用户唯一标识
     */
    @JSONField(name = "openid")
    private String openId;
    /**
     * 会话密钥
     */
    @JSONField(name = "session_key")
    private String sessionKey;
    /**
     * 用户在开放平台的唯一标识符，在满足 UnionID 下发条件的情况下会返回，详见 UnionID 机制说明。
     */
    @JSONField(name = "unionid")
    private String unionId;
    /**
     * 错误码
     */
    @JSONField(name = "errcode")
    private int errCode;
    /**
     * 错误信息
     */
    @JSONField(name = "errmsg")
    private String errMsg;

}
