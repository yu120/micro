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
            request.header(CONTENT_TYPE_KEY, CONTENT_TYPE);
            request.postDataCharset(StandardCharsets.UTF_8.name());
            request.method(Connection.Method.POST);
            request.requestBody(JSON.toJSONString(robotSendRequest));

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

}
