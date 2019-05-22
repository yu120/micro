package cn.micro.biz.pubsrv.dingtalk;

import com.alibaba.fastjson.JSON;
import com.dingtalk.api.DefaultDingTalkClient;
import com.dingtalk.api.DingTalkClient;
import com.dingtalk.api.request.OapiRobotSendRequest;
import com.dingtalk.api.response.OapiRobotSendResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.List;

/**
 * Micro Ding Talk Configuration
 *
 * @author lry
 */
@Slf4j
@Configuration
@EnableConfigurationProperties(MicroDingTalkProperties.class)
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class MicroDingTalkService implements InitializingBean {

    private static final String SERVER_URL = "https://oapi.dingtalk.com/robot/send?access_token=";
    private final MicroDingTalkProperties properties;

    private DingTalkClient defaultDingTalkClient;

    @Override
    public void afterPropertiesSet() throws Exception {
        this.defaultDingTalkClient = new DefaultDingTalkClient(SERVER_URL + properties.getAccessToken());
    }

    public boolean sendText(String content, List<String> atMobiles) throws Exception {
        OapiRobotSendRequest request = new OapiRobotSendRequest();
        request.setMsgtype("text");

        OapiRobotSendRequest.Text text = new OapiRobotSendRequest.Text();
        text.setContent(content);
        request.setText(text);

        // @相关人
        this.wrapperAtMobiles(request, atMobiles);
        return this.sendRequest(request);
    }

    public boolean sendLink(String title, String text, String picUrl, String messageUrl, List<String> atMobiles) throws Exception {
        OapiRobotSendRequest request = new OapiRobotSendRequest();
        request.setMsgtype("link");

        OapiRobotSendRequest.Link link = new OapiRobotSendRequest.Link();
        link.setMessageUrl(messageUrl);
        link.setPicUrl(picUrl);
        link.setTitle(title);
        link.setText(text);
        request.setLink(link);

        // @相关人
        this.wrapperAtMobiles(request, atMobiles);
        return this.sendRequest(request);
    }

    public boolean sendMarkdown(String title, String text, List<String> atMobiles) throws Exception {
        OapiRobotSendRequest request = new OapiRobotSendRequest();
        request.setMsgtype("markdown");

        OapiRobotSendRequest.Markdown markdown = new OapiRobotSendRequest.Markdown();
        markdown.setTitle(title);
        markdown.setText(text);
        request.setMarkdown(markdown);

        // @相关人
        this.wrapperAtMobiles(request, atMobiles);
        return this.sendRequest(request);
    }

    /**
     * 相关人的@功能
     *
     * @param request   {@link OapiRobotSendRequest}
     * @param atMobiles at mobiles list
     */
    private void wrapperAtMobiles(OapiRobotSendRequest request, List<String> atMobiles) {
        if (atMobiles != null && !atMobiles.isEmpty()) {
            OapiRobotSendRequest.At at = new OapiRobotSendRequest.At();
            at.setAtMobiles(atMobiles);
            request.setAt(at);
        }
    }

    /**
     * Send request
     *
     * @param request {@link OapiRobotSendRequest}
     * @return success true
     * @throws Exception throw exception
     */
    private boolean sendRequest(OapiRobotSendRequest request) throws Exception {
        OapiRobotSendResponse response = defaultDingTalkClient.execute(request);
        log.info("Ding Talk response body:{}", JSON.toJSONString(response));
        return response != null && response.getErrcode() == 0;
    }

}
