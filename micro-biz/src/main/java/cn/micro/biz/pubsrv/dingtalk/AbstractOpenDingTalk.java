package cn.micro.biz.pubsrv.dingtalk;

import cn.micro.biz.commons.utils.HttpUtils;
import cn.micro.biz.pubsrv.dingtalk.request.*;
import cn.micro.biz.pubsrv.dingtalk.response.*;
import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;

/**
 * Abstract Open Ding Talk
 *
 * @author lry
 */
@Slf4j
public class AbstractOpenDingTalk {

    private static final int RESPONSE_CODE_OK = 0;
    private static final String ACCESS_TOKEN = "access_token=%s";

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
        String url = req.getUrl();
        if (url.contains(ACCESS_TOKEN)) {
            url = String.format(url, accessToken);
        }

        String responseBody = HttpUtils.sendRequest(req.getMethod(), url, req);
        log.debug("Ding Talk response body:{}", responseBody);
        RES res = JSON.parseObject(responseBody, clazz);
        if (res == null) {
            log.warn("Ding Talk send fail, response body:{}", responseBody);
            throw new RuntimeException(responseBody);
        }
        if (RESPONSE_CODE_OK != res.getErrCode()) {
            throw new RuntimeException(res.getErrCode() + "->" + res.getErrMsg());
        }

        return res;
    }

}
