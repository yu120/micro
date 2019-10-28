package cn.micro.biz.pubsrv.hook;

import com.alibaba.fastjson.JSON;
import lombok.Data;
import lombok.ToString;

import java.io.Serializable;
import java.util.List;

/**
 * Beary Chat Incoming
 *
 * @author lry
 */
public class BearyChatIncoming {

    /**
     * The incoming notify
     *
     * @param body notify body
     * @return success true
     */
    public String incoming(String body) {
        BearyChatIncomingRequest request = JSON.parseObject(body, BearyChatIncomingRequest.class);
        BearyChatIncomingResponse response = new BearyChatIncomingResponse();
        response.setText("这是请求内容：" + request.getText());
        return JSON.toJSONString(response);
    }

    /**
     * Incoming 机器人请求消息体
     *
     * @author lry
     */
    @Data
    @ToString
    public static class BearyChatIncomingRequest implements Serializable {
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
     * Incoming 机器人响应消息体
     *
     * @author lry
     */
    @Data
    @ToString
    public static class BearyChatIncomingResponse implements Serializable {
        private String text;
        private List<BearyChatIncomingAttachment> attachment;
    }

    /**
     * Incoming Attachment
     *
     * @author lry
     */
    @Data
    @ToString
    public static class BearyChatIncomingAttachment implements Serializable {
        private String title;
        private String text;
        private String color;
        private List<BearyChatIncomingImage> images;
    }

    /**
     * Incoming Image
     *
     * @author lry
     */
    @Data
    @ToString
    public static class BearyChatIncomingImage implements Serializable {
        private String url;
    }

}
