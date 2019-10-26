package cn.micro.biz.pubsrv.hook;

import cn.micro.biz.commons.exception.support.MicroErrorException;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Connection;
import org.jsoup.helper.HttpConnection;

import java.io.Serializable;
import java.nio.charset.StandardCharsets;
import java.util.List;

/**
 * Ding Talk Web Hook
 * <p>
 * 每个机器人每分钟最多发送20条。
 *
 * @author lry
 */
@Slf4j
public class DingTalkWebHook implements Serializable {

    private static final int RESPONSE_CODE_OK = 0;
    private static final String RESPONSE_CODE_KEY = "errcode";
    private static final String RESPONSE_MSG_KEY = "errmsg";
    private static final String CONTENT_TYPE_KEY = "Content-Type";
    private static final String CONTENT_TYPE = "application/json";
    private static final String SERVER_URL = "https://oapi.dingtalk.com/robot/send?access_token=%s";

    public static void main(String[] args) throws Exception {
        DingTalkWebHookTextRequest robotSendRequestText = new DingTalkWebHookTextRequest();
        robotSendRequestText.setText(new DingTalkWebHookText("测试机器人功能的消息201905"));
        robotSendRequestText.setAt(new DingTalkWebHookAt(null, true));
        WebHookResult webHookResult = DingTalkWebHook.push(
                "0044bea6737e89921d27495e5d57592ccd10a74ab04a4b39b1ec7ff87db6106c", robotSendRequestText);
        System.out.println(webHookResult);
    }

    /**
     * Send push to 3th
     *
     * @param accessToken      access token
     * @param dingTalkWebHookRequest {@link DingTalkWebHookRequest}
     * @return success true
     */
    public static WebHookResult push(String accessToken, DingTalkWebHookRequest dingTalkWebHookRequest) {
        String url = String.format(SERVER_URL, accessToken);
        Connection.Response response;
        try {
            Connection connection = HttpConnection.connect(url).ignoreContentType(true);
            Connection.Request request = connection.request();
            request.header(CONTENT_TYPE_KEY, CONTENT_TYPE);
            request.postDataCharset(StandardCharsets.UTF_8.name());
            request.method(Connection.Method.POST);
            request.requestBody(JSON.toJSONString(dingTalkWebHookRequest));

            log.debug("Ding Talk request url:[{}], method:[{}], headers:[{}], body:[{}]",
                    url, request.method(), request.headers(), request.requestBody());
            response = connection.execute();
        } catch (Exception e) {
            throw new MicroErrorException(e.getMessage(), e);
        }

        if (200 != response.statusCode()) {
            log.warn("Network error:[code:{},message:{}]", response.statusCode(), response.statusMessage());
            return new WebHookResult(false,
                    response.statusCode() + ":" + response.statusMessage(), response.body());
        } else {
            String responseBody = response.charset(StandardCharsets.UTF_8.name()).body();
            log.debug("Ding Talk response body:{}", responseBody);
            JSONObject jsonObject = JSON.parseObject(responseBody);
            if (jsonObject == null) {
                log.warn("Ding Talk send fail, response body:{}", responseBody);
                return new WebHookResult(false, "response body is null", responseBody);
            }

            int code = jsonObject.getInteger(RESPONSE_CODE_KEY);
            String msg = jsonObject.getString(RESPONSE_MSG_KEY);
            return new WebHookResult(RESPONSE_CODE_OK == code, msg, responseBody);
        }
    }

    /**
     * 相关人的@功能
     *
     * @param request   {@link DingTalkWebHookRequest}
     * @param atMobiles at mobiles list
     */
    private void wrapperAtMobiles(DingTalkWebHookRequest request, List<String> atMobiles) {
        if (atMobiles != null && !atMobiles.isEmpty()) {
            DingTalkWebHookAt at = new DingTalkWebHookAt();
            at.setAtMobiles(atMobiles);
            request.setAt(at);
        }
    }

    /**
     * Ding Talk Web Hook Request
     *
     * @author lry
     */
    @Data
    @ToString
    @AllArgsConstructor
    public static class DingTalkWebHookRequest implements Serializable {
        /**
         * 消息类型
         */
        private String msgtype;
        /**
         * 被@人的手机号
         */
        private DingTalkWebHookAt at;
    }

    /**
     * 文本消息 (text)
     *
     * @author lry
     */
    @Data
    @ToString(callSuper = true)
    @EqualsAndHashCode(callSuper = true)
    public static class DingTalkWebHookTextRequest extends DingTalkWebHookRequest {
        private DingTalkWebHookText text;

        public DingTalkWebHookTextRequest() {
            super("text", null);
        }
    }

    /**
     * 链接 (link)
     *
     * @author lry
     */
    @Data
    @ToString(callSuper = true)
    @EqualsAndHashCode(callSuper = true)
    public static class DingTalkWebHookLinkRequest extends DingTalkWebHookRequest {
        private DingTalkWebHookLink link;

        public DingTalkWebHookLinkRequest() {
            super("link", null);
        }
    }

    /**
     * FeedCard
     *
     * @author lry
     */
    @Data
    @ToString(callSuper = true)
    @EqualsAndHashCode(callSuper = true)
    public static class DingTalkWebHookFeedCardRequest extends DingTalkWebHookRequest {
        private DingTalkWebHookFeedCard feedCard;

        public DingTalkWebHookFeedCardRequest() {
            super("feedCard", null);
        }
    }

    /**
     * Markdown
     *
     * @author lry
     */
    @Data
    @ToString(callSuper = true)
    @EqualsAndHashCode(callSuper = true)
    public static class DingTalkWebHookMarkdownRequest extends DingTalkWebHookRequest {
        private DingTalkWebHookMarkdown markdown;

        public DingTalkWebHookMarkdownRequest() {
            super("markdown", null);
        }
    }

    /**
     * ActionCard
     */
    @Data
    @ToString(callSuper = true)
    @EqualsAndHashCode(callSuper = true)
    public static class DingTalkWebHookActionCardRequest extends DingTalkWebHookRequest {
        private DingTalkWebHookActionCard actionCard;

        public DingTalkWebHookActionCardRequest() {
            super("actionCard", null);
        }
    }

    // === 以下为支持模型

    /**
     * Text
     *
     * @author lry
     */
    @Data
    @ToString
    @NoArgsConstructor
    @AllArgsConstructor
    public static class DingTalkWebHookText implements Serializable {
        private String content;
    }

    /**
     * At
     *
     * @author lry
     */
    @Data
    @ToString
    @NoArgsConstructor
    @AllArgsConstructor
    public static class DingTalkWebHookAt implements Serializable {
        private List<String> atMobiles;
        /**
         * @ 所有人时:true,否则为:false
         */
        private Boolean isAtAll;
    }

    /**
     * Link
     *
     * @author lry
     */
    @Data
    @ToString
    @NoArgsConstructor
    @AllArgsConstructor
    public static class DingTalkWebHookLink implements Serializable {
        /**
         * 点击消息跳转的URL
         */
        private String messageUrl;
        /**
         * 图片URL
         */
        private String picUrl;
        /**
         * 消息内容。如果太长只会部分展示
         */
        private String text;
        /**
         * 消息标题
         */
        private String title;
    }

    /**
     * Markdown
     *
     * @author lry
     */
    @Data
    @ToString
    @NoArgsConstructor
    @AllArgsConstructor
    public static class DingTalkWebHookMarkdown implements Serializable {
        /**
         * markdown格式的消息
         */
        private String text;
        /**
         * 首屏会话透出的展示内容
         */
        private String title;
    }

    /**
     * Btn
     *
     * @author lry
     */
    @Data
    @ToString
    @NoArgsConstructor
    @AllArgsConstructor
    public static class DingTalkWebHookBtn implements Serializable {
        /**
         * 按钮方案，
         */
        private String actionURL;
        /**
         * 点击按钮触发的URL此消息类型为固定feedCard
         */
        private String title;
    }

    /**
     * ActionCard
     *
     * @author lry
     */
    @Data
    @ToString
    @NoArgsConstructor
    @AllArgsConstructor
    public static class DingTalkWebHookActionCard implements Serializable {
        /**
         * 0-按钮竖直排列，1-按钮横向排列
         */
        private String btnOrientation;
        /**
         * 按钮的信息
         */
        private List<DingTalkWebHookBtn> btns;
        /**
         * 0-正常发消息者头像,1-隐藏发消息者头像
         */
        private String hideAvatar;
        /**
         * 单个按钮的方案。(设置此项和singleURL后btns无效。)
         */
        private String singleTitle;
        /**
         * 点击singleTitle按钮触发的URL
         */
        private String singleURL;
        /**
         * markdown格式的消息
         */
        private String text;
        /**
         * 首屏会话透出的展示内容
         */
        private String title;
    }

    /**
     * FeedCard
     *
     * @author lry
     */
    @Data
    @ToString
    @NoArgsConstructor
    @AllArgsConstructor
    public static class DingTalkWebHookFeedCard implements Serializable {
        private List<DingTalkWebHookLinks> links;
    }

    /**
     * Links
     *
     * @author lry
     */
    @Data
    @ToString
    @NoArgsConstructor
    @AllArgsConstructor
    public static class DingTalkWebHookLinks implements Serializable {
        /**
         * 点击单条信息到跳转链接
         */
        private String messageURL;
        /**
         * 单条信息文本
         */
        private String picURL;
        /**
         * 单条信息后面图片的URL
         */
        private String title;
    }

}
