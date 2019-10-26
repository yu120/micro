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
 * 企业 WeChat Web Hook
 * <p>
 * 每个机器人发送的消息不能超过20条/分钟。
 * https://work.weixin.qq.com/api/doc?notreplace=true#90000/90136/91770
 *
 * @author lry
 */
@Slf4j
public class WeChatWebHook implements Serializable {

    private static final int RESPONSE_CODE_OK = 0;
    private static final String RESPONSE_CODE_KEY = "errcode";
    private static final String RESPONSE_MSG_KEY = "errmsg";
    private static final String CONTENT_TYPE_KEY = "Content-Type";
    private static final String CONTENT_TYPE = "application/json";
    private static final String SERVER_URL = "https://qyapi.weixin.qq.com/cgi-bin/webhook/send?key=%s";

    public static void main(String[] args) throws Exception {
        RobotSendRequestText robotSendRequest = new RobotSendRequestText();
        robotSendRequest.setText(new Text("你好呀", null, null));
        WebHookResult webHookResult = WeChatWebHook.push("916d230b-8050-41ad-8bf4-859f76a27eaf", robotSendRequest);
        System.out.println(webHookResult);
    }

    /**
     * Send push to 3th
     *
     * @param accessToken access token
     * @return success true
     */
    public static WebHookResult push(String accessToken, RobotSendRequest robotSendRequest) {
        String url = String.format(SERVER_URL, accessToken);
        Connection.Response response;
        try {
            Connection connection = HttpConnection.connect(url).ignoreContentType(true);
            Connection.Request request = connection.request();
            request.method(Connection.Method.POST);
            request.header(CONTENT_TYPE_KEY, CONTENT_TYPE);
            request.postDataCharset(StandardCharsets.UTF_8.name());
            request.requestBody(JSON.toJSONString(robotSendRequest));

            log.debug("WeChat request url:[{}], method:[{}], headers:[{}], body:[{}]",
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
            log.debug("WeChat response body:{}", responseBody);
            JSONObject jsonObject = JSON.parseObject(responseBody);
            if (jsonObject == null) {
                log.warn("WeChat send fail, response body:{}", responseBody);
                return new WebHookResult(false, "response body is null", responseBody);
            }

            int code = jsonObject.getInteger(RESPONSE_CODE_KEY);
            String msg = jsonObject.getString(RESPONSE_MSG_KEY);
            return new WebHookResult(RESPONSE_CODE_OK == code, msg, responseBody);
        }
    }

    /**
     * Robot Send Request
     *
     * @author lry
     */
    @Data
    @ToString
    @AllArgsConstructor
    public static class RobotSendRequest implements Serializable {
        /**
         * 消息类型
         */
        private String msgtype;
    }

    /**
     * 文本消息 (text)
     *
     * @author lry
     */
    @Data
    @ToString(callSuper = true)
    @EqualsAndHashCode(callSuper = true)
    public static class RobotSendRequestText extends RobotSendRequest {
        private Text text;

        public RobotSendRequestText() {
            super("text");
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
    public static class RobotSendRequestMarkdown extends RobotSendRequest {
        private Markdown markdown;

        public RobotSendRequestMarkdown() {
            super("markdown");
        }
    }

    /**
     * Image
     *
     * @author lry
     */
    @Data
    @ToString(callSuper = true)
    @EqualsAndHashCode(callSuper = true)
    public static class RobotSendRequestImage extends RobotSendRequest {
        private Image image;

        public RobotSendRequestImage() {
            super("image");
        }
    }

    /**
     * News
     *
     * @author lry
     */
    @Data
    @ToString(callSuper = true)
    @EqualsAndHashCode(callSuper = true)
    public static class RobotSendRequestNews extends RobotSendRequest {
        private News news;

        public RobotSendRequestNews() {
            super("news");
        }
    }

    /**
     * Text
     *
     * @author lry
     */
    @Data
    @ToString
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Text implements Serializable {
        /**
         * 文本内容，最长不超过2048个字节，必须是utf8编码
         */
        private String content;
        /**
         * userid的列表，提醒群中的指定成员(@某个成员)，@all表示提醒所有人，如果开发者获取不到userid，可以使用mentioned_mobile_list
         */
        private String mentioned_list;
        /**
         * 手机号列表，提醒手机号对应的群成员(@某个成员)，@all表示提醒所有人
         */
        private String mentioned_mobile_list;
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
    public static class Markdown implements Serializable {
        /**
         * markdown内容，最长不超过4096个字节，必须是utf8编码
         */
        private String content;
    }

    /**
     * Image
     *
     * @author lry
     */
    @Data
    @ToString
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Image implements Serializable {
        /**
         * 图片内容的base64编码
         */
        private String base64;
        /**
         * 图片内容（base64编码前）的md5值
         */
        private String md5;
    }

    /**
     * News
     *
     * @author lry
     */
    @Data
    @ToString
    @NoArgsConstructor
    @AllArgsConstructor
    public static class News implements Serializable {
        /**
         * 图文消息，一个图文消息支持1到8条图文
         */
        private List<Article> articles;
    }

    /**
     * Article
     *
     * @author lry
     */
    @Data
    @ToString
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Article implements Serializable {
        /**
         * 标题，不超过128个字节，超过会自动截断
         */
        private String title;
        /**
         * 描述，不超过512个字节，超过会自动截断
         */
        private String description;
        /**
         * 点击后跳转的链接。
         */
        private String url;
        /**
         * 图文消息的图片链接，支持JPG、PNG格式，较好的效果为大图 1068*455，小图150*150。
         */
        private String picurl;
    }

}
