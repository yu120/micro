package cn.micro.biz.pubsrv.wechat;

import cn.micro.biz.commons.utils.HttpUtils;
import cn.micro.biz.pubsrv.wechat.request.WeChatCode2SessionRequest;
import cn.micro.biz.pubsrv.wechat.request.WeChatRequest;
import cn.micro.biz.pubsrv.wechat.response.WeChatCode2SessionResponse;
import cn.micro.biz.pubsrv.wechat.response.WeChatResponse;
import com.alibaba.fastjson.JSON;
import lombok.AllArgsConstructor;

/**
 * WeChat Mini Program
 *
 * <image src="https://res.wx.qq.com/wxdoc/dist/assets/img/api-login.2fcc9f35.jpg"/>
 *
 * @author lry
 */
@AllArgsConstructor
public class WeChatMiniProgram {

    private static final int RESPONSE_CODE_OK = 0;

    private final String appId;
    private final String appSecret;

    /**
     * Sent request
     *
     * @param req   {@link REQ}
     * @param clazz {@link Class<RES>}
     * @param <REQ> {@link WeChatRequest}
     * @param <RES> {@link WeChatResponse}
     * @return {@link RES}
     */
    private <REQ extends WeChatRequest, RES extends WeChatResponse> RES sendRequest(REQ req, Class<RES> clazz) {
        String responseBody = HttpUtils.sendRequest(req.getMethod(), req.getUrl(), req);
        RES res = JSON.parseObject(responseBody, clazz);
        if (RESPONSE_CODE_OK != res.getErrCode()) {
            throw new RuntimeException(res.getErrCode() + "->" + res.getErrMsg());
        }

        return res;
    }

    /**
     * 登录凭证校验
     * <p>
     * 调用auth.code2Session接口，换取用户唯一标识OpenID和会话密钥session_key
     *
     * @param jsCode 调用wx.login()获取临时登录凭证code，并回传到开发者服务器。
     * @return {@link WeChatCode2SessionResponse}
     */
    public WeChatCode2SessionResponse authCode2Session(String jsCode) {
        WeChatCode2SessionRequest request = new WeChatCode2SessionRequest();
        request.setAppId(appId);
        request.setSecret(appSecret);
        request.setJsCode(jsCode);
        return sendRequest(request, WeChatCode2SessionResponse.class);
    }

}
