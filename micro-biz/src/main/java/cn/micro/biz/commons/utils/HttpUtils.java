package cn.micro.biz.commons.utils;

import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.util.Strings;
import org.jsoup.Connection;
import org.jsoup.helper.HttpConnection;

import java.nio.charset.StandardCharsets;

/**
 * Http Utils
 *
 * @author lry
 */
@Slf4j
public class HttpUtils {

    private static final int HTTP_RESPONSE_CODE_OK = 200;
    private static final String CONTENT_TYPE_KEY = "Content-Type";
    private static final String CONTENT_TYPE = "application/json";

    /**
     * Sent request
     *
     * @param method method
     * @param url    url
     * @param body   request body
     * @return response body
     */
    public static String sendRequest(String method, String url, Object body) {
        String requestBody = null;
        if (body != null) {
            if (body instanceof String) {
                requestBody = body.toString();
            } else {
                requestBody = JSON.toJSONString(body);
            }
        }

        return sendRequest(method, url, requestBody);
    }

    /**
     * Sent request
     *
     * @param method method
     * @param url    url
     * @param body   request body
     * @return response body
     */
    public static String sendRequest(String method, String url, String body) {
        Connection.Response response;
        try {
            Connection connection = HttpConnection.connect(url).ignoreContentType(true);
            Connection.Request request = connection.request();
            request.header(CONTENT_TYPE_KEY, CONTENT_TYPE);
            request.postDataCharset(StandardCharsets.UTF_8.name());
            request.method(Connection.Method.valueOf(method));
            if (Strings.isNotBlank(body)) {
                request.requestBody(body);
            }

            log.debug("Request url:[{}], method:[{}], headers:[{}], body:[{}]",
                    url, request.method(), request.headers(), request.requestBody());
            response = connection.execute();
            log.debug("Response code:[{}], status:[{}]", response.statusCode(), response.statusMessage());
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage(), e);
        }

        if (HTTP_RESPONSE_CODE_OK != response.statusCode()) {
            throw new RuntimeException(response.statusCode() + "->" + response.statusMessage());
        } else {
            String responseBody = response.charset(StandardCharsets.UTF_8.name()).body();
            log.debug("Response body:[{}]", responseBody);
            return responseBody;
        }
    }

}
