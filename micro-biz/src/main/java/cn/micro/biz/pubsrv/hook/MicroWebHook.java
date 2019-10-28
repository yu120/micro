package cn.micro.biz.pubsrv.hook;

import com.alibaba.fastjson.JSON;
import lombok.Data;
import lombok.ToString;

import java.io.Serializable;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Micro Web Hook
 *
 * @author lry
 */
public class MicroWebHook {

    private ConcurrentHashMap<String, String> token = new ConcurrentHashMap<>();

    /**
     * The in coming request process
     *
     * @param token token
     * @param body  request body
     * @return response body
     */
    public String incoming(String token, String body) {
        return bearyChat(body);
    }

    /**
     * The outgoing notify
     *
     * @param body notify body
     * @return success true
     */
    public String bearyChat(String body) {
        BearyChatWebHookOutgoingRequest request = JSON.parseObject(body, BearyChatWebHookOutgoingRequest.class);
        BearyChatWebHookOutgoingResponse response = new BearyChatWebHookOutgoingResponse();
        response.setText("这是请求内容：" + request.getText());
        return JSON.toJSONString(response);
    }

    /**
     * Outgoing 机器人请求消息体
     *
     * @author lry
     */
    @Data
    @ToString
    public static class BearyChatWebHookOutgoingRequest implements Serializable {
        /**
         * 外部服务可以通过验证请求数据中的token是否为指定token来判断请求有效性
         */
        private String token;
        private Long ts;
        private String text;
        /**
         * 触发词
         */
        private String trigger_word;
        private String subdomain;
        private String channel_name;
        private String user_name;
    }

    /**
     * Outgoing 机器人响应消息体
     *
     * @author lry
     */
    @Data
    @ToString
    public static class BearyChatWebHookOutgoingResponse implements Serializable {
        private String text;
        private List<BearyChatWebHookOutgoingAttachment> attachment;
    }

    /**
     * Outgoing Attachment
     *
     * @author lry
     */
    @Data
    @ToString
    public static class BearyChatWebHookOutgoingAttachment implements Serializable {
        private String title;
        private String text;
        private String color;
        private List<BearyChatWebHookOutgoingImage> images;
    }

    /**
     * Outgoing Image
     *
     * @author lry
     */
    @Data
    @ToString
    public static class BearyChatWebHookOutgoingImage implements Serializable {
        private String url;
    }

}
