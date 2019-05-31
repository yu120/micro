package cn.micro.biz.commons.auth;

import com.alibaba.fastjson.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

/**
 * Micro Token
 *
 * @author lry
 */
@Data
@Slf4j
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class MicroToken {

    @JsonProperty("token_type")
    @JSONField(name = "token_type")
    private String tokenType;

    @JsonProperty("access_token")
    @JSONField(name = "access_token")
    private String accessToken;

    @JsonProperty("expires_in")
    @JSONField(name = "expires_in")
    private Long expiresIn;

    @JsonProperty("refresh_token")
    @JSONField(name = "refresh_token")
    private String refreshToken;

    @JsonProperty("refresh_expires_in")
    @JSONField(name = "refresh_expires_in")
    private Long refreshExpiresIn;

    @JsonProperty("scope")
    @JSONField(name = "scope")
    private String scope;

    @JsonProperty("jti")
    @JSONField(name = "jti")
    private String jti;

}
