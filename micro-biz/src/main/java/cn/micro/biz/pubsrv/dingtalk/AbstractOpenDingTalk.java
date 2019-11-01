package cn.micro.biz.pubsrv.dingtalk;

import cn.micro.biz.commons.exception.support.MicroErrorException;
import cn.micro.biz.pubsrv.dingtalk.request.*;
import cn.micro.biz.pubsrv.dingtalk.response.*;
import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.jsoup.Connection;
import org.jsoup.helper.HttpConnection;

import java.nio.charset.StandardCharsets;

/**
 * Abstract Open Ding Talk
 *
 * @author lry
 */
@Slf4j
public class AbstractOpenDingTalk {

    private static final int HTTP_RESPONSE_CODE_OK = 200;
    private static final int RESPONSE_CODE_OK = 0;
    private static final String CONTENT_TYPE_KEY = "Content-Type";
    private static final String CONTENT_TYPE = "application/json";
    private static final String ACCESS_TOKEN = "access_token";

    private final String accessToken;

    public AbstractOpenDingTalk(String accessToken) {
        this.accessToken = accessToken;
    }

    /**
     * Sent request
     *
     * @param req   {@link REQ}
     * @param clazz {@link Class<RES>}
     * @param <REQ> {@link DingTalkRequest}
     * @param <RES> {@link DingTalkResponse}
     * @return {@link RES}
     */
    <REQ extends DingTalkRequest, RES extends DingTalkResponse> RES sendRequest(REQ req, Class<RES> clazz) {
        Connection.Response response;
        String url = req.getUrl();
        if (url.contains(ACCESS_TOKEN)) {
            url = String.format(url, accessToken);
        }

        try {
            Connection connection = HttpConnection.connect(url).ignoreContentType(true);
            Connection.Request request = connection.request();
            request.header(CONTENT_TYPE_KEY, CONTENT_TYPE);
            request.postDataCharset(StandardCharsets.UTF_8.name());
            if (req.getHttpMethod() == HttpMethod.GET) {
                request.method(Connection.Method.GET);
            } else {
                request.method(Connection.Method.POST);
                String requestBody = JSON.toJSONString(req);
                System.out.println(requestBody);
                if (StringUtils.isNotBlank(requestBody)) {
                    request.requestBody(requestBody);
                }
            }

            log.debug("Ding Talk request url:[{}], method:[{}], headers:[{}], body:[{}]",
                    url, request.method(), request.headers(), request.requestBody());
            response = connection.execute();
        } catch (Exception e) {
            throw new MicroErrorException(e.getMessage(), e);
        }

        if (HTTP_RESPONSE_CODE_OK != response.statusCode()) {
            log.warn("Network error:[code:{},message:{}]", response.statusCode(), response.statusMessage());
            throw new RuntimeException(response.statusCode() + "->" + response.statusMessage());
        } else {
            String responseBody = response.charset(StandardCharsets.UTF_8.name()).body();
            System.out.println(responseBody);
            log.debug("Ding Talk response body:{}", responseBody);
            RES res = JSON.parseObject(responseBody, clazz);
            if (res == null) {
                log.warn("Ding Talk send fail, response body:{}", responseBody);
                throw new RuntimeException(response.statusCode() + "->" + response.statusMessage());
            }

            if (RESPONSE_CODE_OK != res.getErrCode()) {
                throw new RuntimeException(res.getErrCode() + "->" + res.getErrMsg());
            }

            return res;
        }
    }

}
