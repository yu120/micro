package cn.micro.biz.pubsrv.webhook;

import cn.micro.biz.commons.exception.MicroErrorException;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import lombok.Data;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Connection;
import org.jsoup.helper.HttpConnection;

import java.io.Serializable;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;

/**
 * Ding Talk Web Hook
 *
 * @author lry
 */
@Slf4j
public class DingTalkWebHook implements IWebHook<DingTalkWebHook.RobotSendRequest> {

    private static final int SUCCESS_STATUS_CODE = 200;
    private static final int RESPONSE_CODE_OK = 0;
    private static final String RESPONSE_CODE_KEY = "errcode";
    private static final String CONTENT_TYPE_KEY = "Content-Type";
    private static final String CONTENT_TYPE = "application/json";
    private static final String SERVER_URL = "https://oapi.dingtalk.com/robot/send?access_token=";

    private String accessToken;

    public DingTalkWebHook(String accessToken) {
        this.accessToken = accessToken;
    }

    public static void main(String[] args) throws Exception {
        DingTalkWebHook dingTalkWebHook = new DingTalkWebHook("0044bea6737e89921d27495e5d57592ccd10a74ab04a4b39b1ec7ff87db6106c");
        boolean flag = dingTalkWebHook.sendText("对对对", Arrays.asList("15828252029"));
        System.out.println(flag);
    }

    /**
     * Send push to 3th
     *
     * @param robotSendRequest {@link RobotSendRequest}
     * @return success true
     */
    @Override
    public boolean incoming(RobotSendRequest robotSendRequest) {
        String url = SERVER_URL + accessToken;
        Connection.Response response;
        try {
            Connection connection = HttpConnection.connect(url).ignoreContentType(true);
            Connection.Request request = connection.request();
            // setter post request header
            request.header(CONTENT_TYPE_KEY, CONTENT_TYPE);
            // setter post request body charset
            request.postDataCharset(StandardCharsets.UTF_8.name());
            // setter post request method
            request.method(Connection.Method.POST);
            // setter post request body
            request.requestBody(JSON.toJSONString(robotSendRequest));

            log.debug("Ding Talk request url:[{}], method:[{}], headers:[{}], body:[{}]",
                    url, request.method(), request.headers(), JSON.toJSONString(request.data()));
            response = connection.execute();
        } catch (Exception e) {
            throw new MicroErrorException(e.getMessage(), e);
        }

        if (SUCCESS_STATUS_CODE != response.statusCode()) {
            throw new MicroErrorException("网络错误, code:" +
                    response.statusCode() + ", message:" + response.statusMessage());
        }

        String responseBody = response.charset(StandardCharsets.UTF_8.name()).body();
        log.debug("Ding Talk response body:{}", responseBody);
        JSONObject jsonObject = JSON.parseObject(responseBody);
        if (jsonObject == null || !jsonObject.containsKey(RESPONSE_CODE_KEY)) {
            log.warn("Ding Talk send fail, response body:{}", responseBody);
            return false;
        }

        return RESPONSE_CODE_OK == jsonObject.getInteger(RESPONSE_CODE_KEY);
    }

    /**
     * 相关人的@功能
     *
     * @param request   {@link RobotSendRequest}
     * @param atMobiles at mobiles list
     */
    private void wrapperAtMobiles(RobotSendRequest request, List<String> atMobiles) {
        if (atMobiles != null && !atMobiles.isEmpty()) {
            At at = new At();
            at.setAtMobiles(atMobiles);
            request.setAt(at);
        }
    }

    public boolean sendText(String content, List<String> atMobiles) throws Exception {
        RobotSendRequest request = new RobotSendRequest();
        request.setMsgtype("text");

        Text text = new Text();
        text.setContent(content);
        request.setText(text);

        this.wrapperAtMobiles(request, atMobiles);
        return this.incoming(request);
    }

    public boolean sendLink(String title, String text, String picUrl, String messageUrl, List<String> atMobiles) throws Exception {
        RobotSendRequest request = new RobotSendRequest();
        request.setMsgtype("link");

        Link link = new Link();
        link.setMessageUrl(messageUrl);
        link.setPicUrl(picUrl);
        link.setTitle(title);
        link.setText(text);
        request.setLink(link);

        this.wrapperAtMobiles(request, atMobiles);
        return this.incoming(request);
    }

    public boolean sendMarkdown(String title, String text, List<String> atMobiles) throws Exception {
        RobotSendRequest request = new RobotSendRequest();
        request.setMsgtype("markdown");

        Markdown markdown = new Markdown();
        markdown.setTitle(title);
        markdown.setText(text);
        request.setMarkdown(markdown);

        this.wrapperAtMobiles(request, atMobiles);
        return this.incoming(request);
    }

    public boolean sendFeedCard(List<Links> links, List<String> atMobiles) throws Exception {
        RobotSendRequest request = new RobotSendRequest();
        request.setMsgtype("feedCard");

        FeedCard feedCard = new FeedCard();
        feedCard.setLinks(links);
        request.setFeedCard(feedCard);

        this.wrapperAtMobiles(request, atMobiles);
        return this.incoming(request);
    }

    /**
     * Robot Send Request
     *
     * @author lry
     */
    @Data
    @ToString
    public static class RobotSendRequest implements IRobotSendRequest {
        /**
         * 消息类型
         */
        private String msgtype;

        /**
         * 被@人的手机号
         */
        private At at;
        /**
         * text类型
         */
        private Text text;
        /**
         * 消息类型，此时固定为:link
         */
        private Link link;
        /**
         * 此消息类型为固定feedCard
         */
        private FeedCard feedCard;
        /**
         * 此消息类型为固定markdown
         */
        private Markdown markdown;
        /**
         * 此消息类型为固定actionCard
         */
        private ActionCard actionCard;
    }

    /**
     * Text
     *
     * @author lry
     */
    @Data
    @ToString
    public static class Text implements Serializable {
        /**
         * text类型
         */
        private String content;
    }

    /**
     * 被@人的手机号
     *
     * @author lry
     */
    @Data
    @ToString
    public static class At implements Serializable {
        /**
         * 被@人的手机号
         */
        private List<String> atMobiles;
        /**
         * @ 所有人时:true,否则为:false
         */
        private String isAtAll;
    }

    /**
     * Link
     *
     * @author lry
     */
    @Data
    @ToString
    public static class Link implements Serializable {
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
    public static class Markdown implements Serializable {
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
     * 按钮的信息
     *
     * @author lry
     */
    @Data
    @ToString
    public static class Btns implements Serializable {
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
    public static class ActionCard implements Serializable {
        /**
         * 0-按钮竖直排列，1-按钮横向排列
         */
        private String btnOrientation;
        /**
         * 按钮的信息
         */
        private List<Btns> btns;
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
     * Links
     *
     * @author lry
     */
    @Data
    @ToString
    public static class Links implements Serializable {
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

    /**
     * FeedCard
     *
     * @author lry
     */
    @Data
    @ToString
    public static class FeedCard implements Serializable {
        private List<Links> links;
    }

}
