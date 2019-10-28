package cn.micro.biz.pubsrv.hook;

import java.util.concurrent.ConcurrentHashMap;

/**
 * Micro Incoming
 *
 * @author lry
 */
public class MicroIncoming {

    private ConcurrentHashMap<String, String> token = new ConcurrentHashMap<>();

    /**
     * The in coming request process
     *
     * @param token token
     * @param body  request body
     * @return response body
     */
    public String incoming(String token, String body) {
        return new BearyChatIncoming().incoming(body);
    }

}
