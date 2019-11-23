package cn.micro.biz.pubsrv.hook;

import cn.micro.biz.commons.utils.HttpUtils;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.annotation.JSONField;
import lombok.*;
import lombok.extern.slf4j.Slf4j;

import java.io.Serializable;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Mp WeChat Outgoing
 * <p>
 * http://mp.weixin.qq.com/wiki?t=resource/res_main&id=mp1433751277&token=&lang=zh_CN
 *
 * @author lry
 */
@Slf4j
public class MpWeChatOutgoing implements Serializable {

    private static final int RESPONSE_CODE_OK = 0;
    private static final String RESPONSE_CODE_KEY = "errcode";
    private static final String RESPONSE_MSG_KEY = "errmsg";
    private static final String SERVER_URL = "https://api.weixin.qq.com/cgi-bin/message/template/send?access_token=%s";

    public static void main(String[] args) throws Exception {
        MpWeChatTemplateOutgoingRequest request = new MpWeChatTemplateOutgoingRequest();
        request.setToUser("onjF-5loEgEtPIXZCe_C6fXVBVc8");
        request.setTemplateId("XzLyIxUojt_bP9NxdQ3vzrzoBb5zTYbLytSkg6v3tvQ");
        request.setUrl("http://www.baidu.com");
        request.getData().put("productType", new WxMpTemplateData("111", "#173177"));
        request.getData().put("name", new WxMpTemplateData("222", "#173177"));
        request.getData().put("number", new WxMpTemplateData("333", "#173177"));
        request.getData().put("expDate", new WxMpTemplateData("444", "#173177"));
        request.getData().put("remark", new WxMpTemplateData("555", "#173177"));

        OutgoingResult outgoingResult = push(
                "27_mX2qeT_IfhIOFTwmCSuxQFJuOw6qpHDw6TElr487zQnXJmxA3IHHXlZ4Ax4NgHndIOgL-xacn1vWuxTc5m8XwePp2I1N2YdP7GFIFFoaFE16gSPIUXNLC_nkxakNGOiADACVD",
                request);
        System.out.println(outgoingResult);
    }

    /**
     * Send push to 3th
     *
     * @param accessToken access token
     * @param request     {@link MpWeChatTemplateOutgoingRequest}
     * @return success true
     */
    public static OutgoingResult push(String accessToken, MpWeChatTemplateOutgoingRequest request) {
        String url = String.format(SERVER_URL, accessToken);
        String responseBody = HttpUtils.sendRequest("POST", url, request);
        log.debug("Mp WeChat response body:{}", responseBody);
        JSONObject jsonObject = JSON.parseObject(responseBody);
        if (jsonObject == null) {
            log.warn("Mp WeChat send fail, response body:{}", responseBody);
            return new OutgoingResult(false, "response body is null", responseBody);
        }

        int code = jsonObject.getInteger(RESPONSE_CODE_KEY);
        String msg = jsonObject.getString(RESPONSE_MSG_KEY);
        return new OutgoingResult(RESPONSE_CODE_OK == code, msg, responseBody);
    }


    /**
     * 模板消息.
     *
     * @author lry
     */
    @Data
    public static class MpWeChatTemplateOutgoingRequest implements Serializable {
        /**
         * 接收者openid.
         */
        @JSONField(name = "touser")
        private String toUser;
        /**
         * 模板ID.
         */
        @JSONField(name = "template_id")
        private String templateId;
        /**
         * 模板跳转链接.
         * <pre>
         * url和miniProgram都是非必填字段，若都不传则模板无跳转；若都传，会优先跳转至小程序。
         * 开发者可根据实际需要选择其中一种跳转方式即可。当用户的微信客户端版本不支持跳小程序时，将会跳转至url。
         * </pre>
         */
        private String url;
        /**
         * 跳小程序所需数据，不需跳小程序可不用传该数据.
         */
        @JSONField(name = "mini_program")
        private MiniProgram miniProgram;
        /**
         * 模板数据.
         * <p>
         * key=参考key
         */
        private Map<String, WxMpTemplateData> data = new LinkedHashMap<>();
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class WxMpTemplateData implements Serializable {
        private String value;
        private String color;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class MiniProgram implements Serializable {
        private String appid;
        private String pagePath;
        /**
         * 是否使用path，否则使用pagepath.
         * 加入此字段是基于微信官方接口变化多端的考虑
         */
        private boolean usePath = true;
    }

}
