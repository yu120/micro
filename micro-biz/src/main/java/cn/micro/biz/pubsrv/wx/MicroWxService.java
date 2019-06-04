package cn.micro.biz.pubsrv.wx;

import cn.micro.biz.commons.exception.support.MicroBadRequestException;
import com.alibaba.fastjson.JSON;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Connection;
import org.jsoup.helper.HttpConnection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.nio.charset.StandardCharsets;

/**
 * Micro Wx Service
 *
 * @author lry
 */
@Slf4j
@Getter
@Configuration
@EnableConfigurationProperties(MicroWxProperties.class)
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class MicroWxService {

    private static final int ERR_CODE_DEFAULT = 0;
    private static final String WX_AUTH_CODE2SESSION_URL = "https://api.weixin.qq.com/sns/jscode2session";

    private final MicroWxProperties properties;

    public WxAuthCode2Session wxLogin(String jsCode) {
        Connection.Response response;
        try {
            Connection connection = HttpConnection.connect(WX_AUTH_CODE2SESSION_URL).ignoreContentType(true);
            Connection.Request request = connection.request();
            // setter get request parameter
            request.postDataCharset(StandardCharsets.UTF_8.name());
            request.data(HttpConnection.KeyVal.create("appid", properties.getAppId()));
            request.data(HttpConnection.KeyVal.create("secret", properties.getSecret()));
            request.data(HttpConnection.KeyVal.create("js_code", jsCode));
            request.data(HttpConnection.KeyVal.create("grant_type", " authorization_code"));
            // setter get request method
            request.method(Connection.Method.GET);
            log.debug("WX auth.code2Session request url:[{}], method:[{}], parameter:[{}]",
                    request.url().toString(), request.method(), JSON.toJSONString(request.data()));
            response = connection.execute();
        } catch (Exception e) {
            log.error("微信登录异常：" + e.getMessage(), e);
            throw new MicroBadRequestException("登录异常");
        }

        String responseBody = response.charset(StandardCharsets.UTF_8.name()).body();
        log.debug("WX auth.code2Session response body:{}", responseBody);
        WxAuthCode2Session wxAuthCode2Session = JSON.parseObject(responseBody, WxAuthCode2Session.class);
        if (ERR_CODE_DEFAULT != wxAuthCode2Session.getErrCode()) {
            log.warn("WX登录失败:{}", responseBody);
            throw new MicroBadRequestException("登录异常");
        }

        return wxAuthCode2Session;
    }

}
