package cn.micro.biz.pubsrv.dingtalk.response;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 获取access_token响应
 *
 * @author lry
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class DingTalkGetTokenResponse extends DingTalkResponse {

    @JSONField(name = "access_token")
    private String accessToken;
    @JSONField(name = "expires_in")
    private Long expiresIn;

}
