package cn.micro.biz.pubsrv.dingtalk;

import cn.micro.biz.pubsrv.dingtalk.request.*;
import cn.micro.biz.pubsrv.dingtalk.response.*;
import lombok.extern.slf4j.Slf4j;

/**
 * Open Ding Talk
 *
 * @author lry
 */
@Slf4j
public class OpenDingTalk extends AbstractOpenDingTalk {

    private static final Long AGENT_ID = 311005612L;
    private static final String APP_KEY = "dingc2fnubihvoqkfsxd";
    private static final String APP_SECRET = "_OxP_nTCNqMs_qkYdqa8ffRHopqehtb-wCITeIcuptbqCee42enCko8VyN5_cO9J";
    private static final String ACCESS_TOKEN = "d350d87a70453e18ab1cbe138441aa3b";

    public OpenDingTalk() {
        super(ACCESS_TOKEN);
    }

    public static void main(String[] args) {
        OpenDingTalk openDingTalk = new OpenDingTalk();
//         System.out.println(openDingTalk.getToken());

//        DingTalkMessageAsyncRequest request = new DingTalkMessageAsyncRequest();
//        request.setToAllUser(true);
//        DingTalkMessageAsyncRequest.Msg msg = new DingTalkMessageAsyncRequest.Msg();
//        msg.setMsgType(DingTalkMessageAsyncRequest.MsgType.TEXT);
//        msg.setText(new DingTalkMessageAsyncRequest.Text("你好呀"));
//        request.setMsg(msg);
//        System.out.println(openDingTalk.messageAsync(request));

        DingTalkMessageGetSendResultRequest request = new DingTalkMessageGetSendResultRequest();
        request.setTaskId(54302353656L);
        System.out.println(openDingTalk.messageGetSendResult(request));
    }

    /**
     * 获取access_token
     * <p>
     * 【注意】正常情况下access_token有效期为7200秒，有效期内重复获取返回相同结果，并自动续期。
     *
     * @return {@link DingTalkGetTokenResponse}
     */
    public DingTalkGetTokenResponse getToken() {
        DingTalkGetTokenRequest request = new DingTalkGetTokenRequest();
        request.setAppKey(APP_KEY);
        request.setAppSecret(APP_SECRET);
        request.setHttpMethod(HttpMethod.GET);
        request.setUrl(String.format(request.getUrl(), APP_KEY, APP_SECRET));
        return sendRequest(request, DingTalkGetTokenResponse.class);
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
     * @param request {@link DingTalkMessageAsyncRequest}
     * @return {@link DingTalkMessageAsyncRequest}
     */
    public DingTalkMessageAsyncResponse messageAsync(DingTalkMessageAsyncRequest request) {
        request.setAgentId(AGENT_ID);
        return sendRequest(request, DingTalkMessageAsyncResponse.class);
    }

    /**
     * 查询工作通知消息的发送进度
     *
     * @param request {@link DingTalkMessageGetSendProgressRequest}
     * @return {@link DingTalkMessageGetSendProgressResponse}
     */
    public DingTalkMessageGetSendProgressResponse messageGetSendResult(DingTalkMessageGetSendProgressRequest request) {
        request.setAgentId(AGENT_ID);
        return sendRequest(request, DingTalkMessageGetSendProgressResponse.class);
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
     * @param request {@link DingTalkMessageGetSendResultRequest}
     * @return {@link DingTalkMessageGetSendResultResponse}
     */
    public DingTalkMessageGetSendResultResponse messageGetSendResult(DingTalkMessageGetSendResultRequest request) {
        request.setAgentId(AGENT_ID);
        return sendRequest(request, DingTalkMessageGetSendResultResponse.class);
    }

    /**
     * 工作通知消息撤回
     *
     * @param request {@link DingTalkMessageRecallRequest}
     * @return {@link DingTalkMessageRecallResponse}
     */
    public DingTalkMessageRecallResponse messageRecall(DingTalkMessageRecallRequest request) {
        request.setAgentId(AGENT_ID);
        return sendRequest(request, DingTalkMessageRecallResponse.class);
    }

}
