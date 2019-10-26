package cn.micro.biz.pubsrv.webhook;

import cn.micro.biz.commons.exception.support.MicroErrorException;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import lombok.Data;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Connection;
import org.jsoup.helper.HttpConnection;

import java.io.Serializable;
import java.nio.charset.StandardCharsets;
import java.util.List;

/**
 * Beary Chat Web Hook
 *
 * @author lry
 */
@Slf4j
public class BearyChatWebHook implements Serializable {

    private static final int RESPONSE_CODE_OK = 0;
    private static final String RESPONSE_CODE_KEY = "code";
    private static final String CONTENT_TYPE_KEY = "Content-Type";
    private static final String CONTENT_TYPE = "application/json";
    private static final String SERVER_URL = "https://hook.bearychat.com/=bwCqE/incoming/%s";

    public static void main(String[] args) throws Exception {
        RobotSendRequest robotSendRequest = new RobotSendRequest();
        robotSendRequest.setText("看看快快快dddddd22222");
        boolean flag = BearyChatWebHook.push("fe3901f23862dca2e15f4695bf845bdd", robotSendRequest);
        System.out.println(flag);
    }

    /**
     * Send push to 3th
     *
     * @param accessToken      access token
     * @param robotSendRequest {@link RobotSendRequest}
     * @return success true
     */
    public static boolean push(String accessToken, RobotSendRequest robotSendRequest) {
        String url = String.format(SERVER_URL, accessToken);
        Connection.Response response;
        try {
            Connection connection = HttpConnection.connect(url).ignoreContentType(true);
            Connection.Request request = connection.request();
            request.header(CONTENT_TYPE_KEY, CONTENT_TYPE);
            request.method(Connection.Method.POST);
            request.postDataCharset(StandardCharsets.UTF_8.name());
            request.requestBody(JSON.toJSONString(robotSendRequest));

            log.debug("Beary Chat request url:[{}], method:[{}], headers:[{}], body:[{}]",
                    url, request.method(), request.headers(), request.requestBody());
            response = connection.execute();
        } catch (Exception e) {
            throw new MicroErrorException(e.getMessage(), e);
        }

        if (200 != response.statusCode()) {
            log.warn("Network error:[code:{},message:{}]", response.statusCode(), response.statusMessage());
            return false;
        } else {
            String responseBody = response.charset(StandardCharsets.UTF_8.name()).body();
            log.debug("Beary Chat response body:{}", responseBody);
            JSONObject jsonObject = JSON.parseObject(responseBody);
            if (jsonObject == null || !jsonObject.containsKey(RESPONSE_CODE_KEY)) {
                log.warn("Beary Chat send fail, response body:{}", responseBody);
                return false;
            }

            return RESPONSE_CODE_OK == jsonObject.getInteger(RESPONSE_CODE_KEY);
        }
    }

    @Data
    @ToString
    public static class RobotSendRequest implements Serializable {
        /**
         * 必须字段。支持 inline md 的文本内容
         */
        private String text;
        /**
         * 可选字段，用户名，邮箱或者手机。在未指定 channel 时如果指定该字段并且该团队中有对应的成员，
         * 消息会发送到该成员和 BearyBot 的私聊会话中。注：如果是手机或者邮箱，该字段必须是验证过的
         */
        private String user;
        /**
         * 可选字段，讨论组名称。如果有该字段并且该讨论组对于机器人创建者可见，消息会发送到指定讨论组中
         */
        private String channel;
        /**
         * 可选字段。用于控制 text 是否解析为 markdown，默认为 true
         */
        private boolean markdown = true;
        /**
         * 可选字段。用于控制消息提醒的内容显示
         */
        private String notification;
        /**
         * 可选字段，一系列附件
         */
        private List<Attachments> attachments;
    }

    /**
     * title和text字段必须有一个
     *
     * @author lry
     */
    @Data
    @ToString
    public static class Attachments implements Serializable {
        /**
         * 可选
         */
        private String title;
        /**
         * 可选，必需带有 scheme，当同时传有 title 和 url 时，title 字段会带超链接
         */
        private String url;
        /**
         * 可选
         */
        private String text;
        /**
         * 可选，用于控制 attachment 在排版时左侧的竖线分隔符颜色
         */
        private String color;
        /**
         * 可选。用于在推送中推送图片，可以最多同时推送3个图片。使用这个字段需要注意，
         * 服务器在收到带images的请求时会主动抓取一次图片内容并缓存，这个过程会比较慢，
         * 可能造成请求响应时间增加。另外如果两次推送的图片地址都一样，那么第二次的响应
         * 时间会显著降低，因为服务器会对请求进行缓存至少一天，所以如果需要不同的图片请使用不同地址。
         */
        private List<ImagesUrl> images;
    }

    @Data
    @ToString
    public static class ImagesUrl implements Serializable {
        private String url;
    }

}
