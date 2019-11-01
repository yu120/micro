package cn.micro.biz.pubsrv.wechat;

import cn.micro.biz.commons.utils.HttpUtils;
import com.alibaba.fastjson.JSON;

/**
 * WeChat Mini Program
 *
 * <image src="https://res.wx.qq.com/wxdoc/dist/assets/img/api-login.2fcc9f35.jpg"/>
 *
 * @author lry
 */
public class WeChatMiniProgram {

    private static final int RESPONSE_CODE_OK = 0;

    /**
     * Sent request
     *
     * @param req   {@link REQ}
     * @param clazz {@link Class<RES>}
     * @param <REQ> {@link WeChatRequest}
     * @param <RES> {@link WeChatResponse}
     * @return {@link RES}
     */
    <REQ extends WeChatRequest, RES extends WeChatResponse> RES sendRequest(REQ req, Class<RES> clazz) {
        String responseBody = HttpUtils.sendRequest(req.getMethod(), req.getUrl(), req);
        RES res = JSON.parseObject(responseBody, clazz);
        if (RESPONSE_CODE_OK != res.getErrCode()) {
            throw new RuntimeException(res.getErrCode() + "->" + res.getErrMsg());
        }

        return res;
    }

    public void s() {

    }

}
