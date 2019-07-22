package cn.micro.biz.pubsrv;

import cn.micro.biz.commons.exception.support.MicroErrorException;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Connection;
import org.jsoup.helper.HttpConnection;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

/**
 * The Abstract Client
 *
 * @author lry
 */
@Slf4j
@RequiredArgsConstructor
public abstract class AbstractHttp {

    private static final int SUCCESS_STATUS_CODE = 200;
    private static final String RESPONSE_CODE = "code";

    private String uri;

    public AbstractHttp(String uri) {
        this.uri = uri;
    }

    /**
     * The send request
     *
     * @param actionEnum {@link ActionEnum}
     * @param obj        object
     * @return response body json
     */
    protected JSONObject request(ActionEnum actionEnum, Object obj) {
        return this.request(actionEnum, JSON.parseObject(JSON.toJSONString(obj),
                new TypeReference<Map<String, Object>>() {
                }));
    }

    /**
     * The send request
     *
     * @param actionEnum {@link ActionEnum}
     * @param args       key1,,value1,......
     * @return response body json
     */
    protected JSONObject request(ActionEnum actionEnum, String... args) {
        Map<String, String> parameters = new HashMap<>();
        for (int i = 0; i < args.length; i += 2) {
            parameters.put(args[i], args[i + 1]);
        }

        return this.request(actionEnum, parameters);
    }

    /**
     * The send request
     *
     * @param actionEnum {@link ActionEnum}
     * @param parameters key1=value1,......
     * @return response body json
     */
    protected JSONObject request(ActionEnum actionEnum, Map<String, Object> parameters) {
        String url = uri + actionEnum.getValue();

        Connection.Response response;
        try {
            Connection connection = HttpConnection.connect(url).ignoreContentType(true);
            Connection.Request request = connection.request();
            // setter post request body
            request.postDataCharset(StandardCharsets.UTF_8.name());
            if (parameters != null && !parameters.isEmpty()) {
                for (Map.Entry<String, Object> entry : parameters.entrySet()) {
                    if (!this.isPrimitive(entry.getValue())) {
                        throw new IllegalArgumentException("不能包含常用数据类型");
                    }
                    String value = URLEncoder.encode(String.valueOf(entry.getValue()),
                            StandardCharsets.UTF_8.name());
                    request.data(HttpConnection.KeyVal.create(entry.getKey(), value));
                }
            }
            // setter post request method
            request.method(Connection.Method.POST);
            log.debug("The request url:[{}], method:[{}], headers:[{}], body:[{}]",
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
        log.debug("The response body:{}", responseBody);
        JSONObject jsonObject = JSON.parseObject(responseBody);
        if (jsonObject == null || !jsonObject.containsKey(RESPONSE_CODE)) {
            log.warn("非法响应报文:{}", responseBody);
            throw new MicroErrorException("非法响应报文");
        }

        int code = jsonObject.getInteger(RESPONSE_CODE);
        if (SUCCESS_STATUS_CODE != code) {
            log.warn("业务失败:{}", responseBody);
        }

        return jsonObject;
    }

    /**
     * 判断是否为常规类型
     *
     * @param obj obj
     * @return true表示为常规对象
     */
    private boolean isPrimitive(Object obj) {
        return obj instanceof String ||
                obj instanceof Number ||
                obj instanceof Boolean ||
                obj.getClass().isPrimitive();
    }

}
