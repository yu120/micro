package cn.micro.biz.pubsrv.dingtalk;

import cn.micro.biz.commons.exception.support.MicroErrorException;
import cn.micro.biz.pubsrv.dingtalk.request.DingTalkGetTokenRequest;
import cn.micro.biz.pubsrv.dingtalk.request.DingTalkMessageAsyncRequest;
import cn.micro.biz.pubsrv.dingtalk.request.DingTalkRequest;
import cn.micro.biz.pubsrv.dingtalk.response.DingTalkGetTokenResponse;
import cn.micro.biz.pubsrv.dingtalk.response.DingTalkMessageAsyncResponse;
import cn.micro.biz.pubsrv.dingtalk.response.DingTalkResponse;
import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.jsoup.Connection;
import org.jsoup.helper.HttpConnection;

import java.nio.charset.StandardCharsets;

/**
 * Open Ding Talk
 *
 * @author lry
 */
@Slf4j
public class OpenDingTalk {

    private static final int HTTP_RESPONSE_CODE_OK = 200;
    private static final int RESPONSE_CODE_OK = 0;
    private static final String RESPONSE_CODE_KEY = "errcode";
    private static final String RESPONSE_MSG_KEY = "errmsg";
    private static final String CONTENT_TYPE_KEY = "Content-Type";
    private static final String CONTENT_TYPE = "application/json";

    private static final Long AGENT_ID = 311005612L;
    private static final String APP_KEY = "dingc2fnubihvoqkfsxd";
    private static final String APP_SECRET = "_OxP_nTCNqMs_qkYdqa8ffRHopqehtb-wCITeIcuptbqCee42enCko8VyN5_cO9J";
    private static final String ACCESS_TOKEN = "d350d87a70453e18ab1cbe138441aa3b";


    public static void main(String[] args) {
        OpenDingTalk openDingTalk = new OpenDingTalk();
        // System.out.println(openDingTalk.getToken());

        DingTalkMessageAsyncRequest request = new DingTalkMessageAsyncRequest();
        request.setToAllUser(true);
        DingTalkMessageAsyncRequest.Msg msg = new DingTalkMessageAsyncRequest.Msg();
        msg.setMsgType(DingTalkMessageAsyncRequest.MsgType.TEXT);
        msg.setText(new DingTalkMessageAsyncRequest.Text("你好呀"));
        request.setMsg(msg);
        System.out.println(openDingTalk.messageAsync(request));
    }

    /**
     * 获取access_token
     * <p>
     * 【注意】正常情况下access_token有效期为7200秒，有效期内重复获取返回相同结果，并自动续期。
     *
     * @return {@link DingTalkGetTokenResponse}
     */
    public DingTalkGetTokenResponse getToken() {
        String url = String.format(DingTalkGetTokenRequest.URL, APP_KEY, APP_SECRET);
        DingTalkGetTokenRequest request = new DingTalkGetTokenRequest();
        request.setAppkey(APP_KEY);
        request.setAppsecret(APP_SECRET);
        request.setHttpMethod(HttpMethod.GET);
        return sendRequest(url, request, DingTalkGetTokenResponse.class);
    }

    /**
     * 发送工作通知消息
     * <p>
     * 发送工作通知消息需要注意以下事项：
     * <p>
     * 1.同一个微应用相同消息的内容同一个用户一天只能接收一次。
     * 2.同一个微应用给同一个用户发送消息，企业内部开发方式一天不得超过500次。
     * 3.通过设置to_all_user参数全员推送消息，一天最多3次。
     * 4.详细的限制说明，请参考“工作通知消息的限制”。
     * 5.该接口是异步发送消息，接口返回成功并不表示用户一定会收到消息，需要通过“查询工作通知消息的发送结果”接口查询是否给用户发送成功。
     * 6.消息类型和样例可参考消息类型文档。
     *
     * @return {@link DingTalkGetTokenResponse}
     */
    public DingTalkMessageAsyncResponse messageAsync(DingTalkMessageAsyncRequest request) {
        String url = String.format(DingTalkMessageAsyncRequest.URL, ACCESS_TOKEN);
        request.setAgentId(AGENT_ID);
        request.setHttpMethod(HttpMethod.POST);
        return sendRequest(url, request, DingTalkMessageAsyncResponse.class);
    }

    private <REQ extends DingTalkRequest, RES extends DingTalkResponse> RES sendRequest(String url, REQ req, Class<RES> clazz) {
        Connection.Response response;
        try {
            Connection connection = HttpConnection.connect(url).ignoreContentType(true);
            Connection.Request request = connection.request();
            request.header(CONTENT_TYPE_KEY, CONTENT_TYPE);
            request.postDataCharset(StandardCharsets.UTF_8.name());
            switch (req.getHttpMethod()) {
                case GET:
                    request.method(Connection.Method.GET);
                    break;
                case POST: {
                    request.method(Connection.Method.POST);
                    String requestBody = JSON.toJSONString(req);
                    System.out.println(requestBody);
                    if (StringUtils.isNotBlank(requestBody)) {
                        request.requestBody(requestBody);
                    }
                    break;
                }
                default:
            }

            log.debug("Ding Talk request url:[{}], method:[{}], headers:[{}], body:[{}]",
                    url, request.method(), request.headers(), request.requestBody());
            response = connection.execute();
        } catch (Exception e) {
            throw new MicroErrorException(e.getMessage(), e);
        }

        if (HTTP_RESPONSE_CODE_OK != response.statusCode()) {
            log.warn("Network error:[code:{},message:{}]", response.statusCode(), response.statusMessage());
            throw new RuntimeException(response.statusCode() + "->" + response.statusMessage());
        } else {
            String responseBody = response.charset(StandardCharsets.UTF_8.name()).body();
            log.debug("Ding Talk response body:{}", responseBody);
            RES res = JSON.parseObject(responseBody, clazz);
            if (res == null) {
                log.warn("Ding Talk send fail, response body:{}", responseBody);
                throw new RuntimeException(response.statusCode() + "->" + response.statusMessage());
            }

            if (RESPONSE_CODE_OK != res.getErrCode()) {
                throw new RuntimeException(res.getErrCode() + "->" + res.getErrMsg());
            }

            return res;
        }
    }

}
