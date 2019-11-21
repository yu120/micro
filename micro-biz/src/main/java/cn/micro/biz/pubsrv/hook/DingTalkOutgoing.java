package cn.micro.biz.pubsrv.hook;

import cn.micro.biz.commons.utils.HttpUtils;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import lombok.*;
import lombok.extern.slf4j.Slf4j;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.io.Serializable;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.List;

/**
 * Ding Talk Outgoing
 * <p>
 * 每个机器人每分钟最多发送20条。
 * https://ding-doc.dingtalk.com/doc#/serverapi2/qf2nxq
 *
 * @author lry
 */
@Slf4j
public class DingTalkOutgoing implements Serializable {

    private static final int RESPONSE_CODE_OK = 0;
    private static final String RESPONSE_CODE_KEY = "errcode";
    private static final String RESPONSE_MSG_KEY = "errmsg";
    private static final String CONTENT_TYPE_KEY = "Content-Type";
    private static final String CONTENT_TYPE = "application/json";

    private static final String SECRET_KEY = "HmacSHA256";
    private static final String SERVER_URL = "https://oapi.dingtalk.com/robot/send?access_token=%s&timestamp=%s&sign=%s";

    public static void main(String[] args) throws Exception {
        DingTalkOutgoingTextRequest dingTalkOutgoingTextRequest = new DingTalkOutgoingTextRequest();
        dingTalkOutgoingTextRequest.setText(new DingTalkOutgoingText("测试机器人功能的消息201905"));
        dingTalkOutgoingTextRequest.setAt(new DingTalkOutgoingAt(null, true));
        OutgoingResult outgoingResult = DingTalkOutgoing.push(
                "SECe6439e9c8d5ddde21cc271f6c83f205f635bd8cb63bb2af893b6019a93d4ef80",
                "cf0cd4f757a3c5e0cba3e05ddd7edbe5212be0b14ad9ecf5990a934b83cd84c0",
                dingTalkOutgoingTextRequest);
        System.out.println(outgoingResult);
    }

    /**
     * Send push to 3th
     *
     * @param secret                  secret
     * @param accessToken             access token
     * @param dingTalkOutgoingRequest {@link DingTalkOutgoingRequest}
     * @return success true
     */
    public static OutgoingResult push(String secret, String accessToken, DingTalkOutgoingRequest dingTalkOutgoingRequest) {
        long timestamp = System.currentTimeMillis();
        String sign = buildSign(timestamp, secret);
        String url = String.format(SERVER_URL, accessToken, timestamp, sign);
        String responseBody = HttpUtils.sendRequest("POST", url, dingTalkOutgoingRequest);
        log.debug("Ding Talk response body:{}", responseBody);
        JSONObject jsonObject = JSON.parseObject(responseBody);
        if (jsonObject == null) {
            log.warn("Ding Talk send fail, response body:{}", responseBody);
            return new OutgoingResult(false, "response body is null", responseBody);
        }

        int code = jsonObject.getInteger(RESPONSE_CODE_KEY);
        String msg = jsonObject.getString(RESPONSE_MSG_KEY);
        return new OutgoingResult(RESPONSE_CODE_OK == code, msg, responseBody);
    }

    /**
     * 获取请求签名
     *
     * @param timestamp current timestamp
     * @param secret    secret
     * @return sign
     */
    private static String buildSign(long timestamp, String secret) {
        try {
            String stringToSign = timestamp + "\n" + secret;
            Mac mac = Mac.getInstance(SECRET_KEY);
            mac.init(new SecretKeySpec(secret.getBytes(StandardCharsets.UTF_8), SECRET_KEY));
            byte[] signData = mac.doFinal(stringToSign.getBytes(StandardCharsets.UTF_8));
            return URLEncoder.encode(new String(Base64.getEncoder().encode(signData)), StandardCharsets.UTF_8.name());
        } catch (Exception e) {
            throw new RuntimeException("The sign exception", e);
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
    public static class DingTalkOutgoingRequest implements Serializable {
        /**
         * 消息类型
         */
        private String msgtype;
        /**
         * 被@人的规则
         */
        private DingTalkOutgoingAt at;
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
    public static class DingTalkOutgoingAt implements Serializable {
        /**
         * 被@人的手机号(在text内容里要有@手机号)
         */
        private List<String> atMobiles;
        /**
         * @ 所有人时:true,否则为:false
         */
        private Boolean isAtAll;
    }

    /**
     * Text
     *
     * @author lry
     */
    @Data
    @ToString(callSuper = true)
    @EqualsAndHashCode(callSuper = true)
    public static class DingTalkOutgoingTextRequest extends DingTalkOutgoingRequest {
        private DingTalkOutgoingText text;

        public DingTalkOutgoingTextRequest() {
            super("text", null);
        }
    }

    /**
     * Link
     *
     * @author lry
     */
    @Data
    @ToString(callSuper = true)
    @EqualsAndHashCode(callSuper = true)
    public static class DingTalkOutgoingLinkRequest extends DingTalkOutgoingRequest {
        private DingTalkOutgoingLink link;

        public DingTalkOutgoingLinkRequest() {
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
    public static class DingTalkOutgoingFeedCardRequest extends DingTalkOutgoingRequest {
        private DingTalkOutgoingFeedCard feedCard;

        public DingTalkOutgoingFeedCardRequest() {
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
    public static class DingTalkOutgoingMarkdownRequest extends DingTalkOutgoingRequest {
        private DingTalkOutgoingMarkdown markdown;

        public DingTalkOutgoingMarkdownRequest() {
            super("markdown", null);
        }
    }

    /**
     * ActionCard
     */
    @Data
    @ToString(callSuper = true)
    @EqualsAndHashCode(callSuper = true)
    public static class DingTalkOutgoingActionCardRequest extends DingTalkOutgoingRequest {
        private DingTalkOutgoingActionCard actionCard;

        public DingTalkOutgoingActionCardRequest() {
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
    public static class DingTalkOutgoingText implements Serializable {
        private String content;
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
    public static class DingTalkOutgoingLink implements Serializable {
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
    public static class DingTalkOutgoingMarkdown implements Serializable {
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
    public static class DingTalkOutgoingBtn implements Serializable {
        /**
         * 按钮标题
         */
        private String title;
        /**
         * 按钮连接
         */
        private String actionURL;
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
    public static class DingTalkOutgoingActionCard implements Serializable {
        /**
         * 0-按钮竖直排列，1-按钮横向排列
         */
        private String btnOrientation;
        /**
         * 按钮的信息
         */
        private List<DingTalkOutgoingBtn> btns;
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
    public static class DingTalkOutgoingFeedCard implements Serializable {
        private List<DingTalkOutgoingLinks> links;
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
    public static class DingTalkOutgoingLinks implements Serializable {
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
