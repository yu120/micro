package cn.micro.biz.pubsrv.hook;

import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.cp.api.impl.WxCpServiceImpl;
import me.chanjar.weixin.cp.bean.WxCpMessage;
import me.chanjar.weixin.cp.bean.WxCpMessageSendResult;
import me.chanjar.weixin.cp.config.impl.WxCpDefaultConfigImpl;

import java.io.Serializable;

/**
 * Cp WeChat App Outgoing
 * <p>
 * https://blog.csdn.net/zxl782340680/article/details/79876502
 *
 * @author lry
 */
@Slf4j
public class CpWeChatAppOutgoing implements Serializable {

    private static final int RESPONSE_CODE_OK = 0;
    private static final String RESPONSE_CODE_KEY = "errcode";
    private static final String RESPONSE_MSG_KEY = "errmsg";
    private static final String SERVER_URL = "https://api.weixin.qq.com/cgi-bin/message/template/send?access_token=%s";

    public static void main(String[] args) throws Exception {
        WxCpDefaultConfigImpl config = new WxCpDefaultConfigImpl();
        config.setCorpId("wwb33282d3c98d0c37");
        config.setCorpSecret("WqkvoDvWalVGpnhsUGTJeDUd0Ij_sIC4M6B_YI57HaI");
        config.setAgentId(1000002);

        WxCpServiceImpl wxCpService = new WxCpServiceImpl();
        wxCpService.setWxCpConfigStorage(config);

        String userId = "LiRuYu";
        WxCpMessage message = WxCpMessage.TEXT().toUser(userId).content("Hello World").build();
        WxCpMessageSendResult result = wxCpService.messageSend(message);
        System.out.println(result);
    }

}
