package cn.micro.biz.pubsrv.sms;

import cn.micro.biz.commons.exception.support.MicroErrorException;
import cn.micro.biz.commons.trace.Trace;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.dysmsapi.model.v20170525.SendSmsRequest;
import com.aliyuncs.dysmsapi.model.v20170525.SendSmsResponse;
import com.aliyuncs.http.HttpClientConfig;
import com.aliyuncs.http.MethodType;
import com.aliyuncs.profile.DefaultProfile;
import com.aliyuncs.profile.IClientProfile;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Service;

/**
 * Sms Service
 *
 * @author lry
 */
@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@EnableConfigurationProperties(AliYunSmsProperties.class)
@ConditionalOnProperty(prefix = "micro.sms.ali-yun", name = "enable", havingValue = "true")
public class AliYunSmsService implements InitializingBean, DisposableBean {

    private final AliYunSmsProperties properties;
    private IAcsClient acsClient;

    @Override
    public void afterPropertiesSet() throws Exception {
        if (!properties.isEnable()) {
            return;
        }

        DefaultProfile.addEndpoint("cn-hangzhou",
                "cn-hangzhou", "Dysmsapi", "dysmsapi.aliyuncs.com");
        IClientProfile profile = DefaultProfile.getProfile(
                "cn-hangzhou", properties.getAccessKey(), properties.getSecretKey());
        HttpClientConfig httpClientConfig = HttpClientConfig.getDefault();
        httpClientConfig.setReadTimeoutMillis(properties.getReadTimeoutMillis());
        httpClientConfig.setWriteTimeoutMillis(properties.getWriteTimeoutMillis());
        httpClientConfig.setConnectionTimeoutMillis(properties.getConnectionTimeoutMillis());
        profile.setHttpClientConfig(httpClientConfig);
        this.acsClient = new DefaultAcsClient(profile);
    }

    @Trace
    public boolean send(String templateCode, String mobile, Object... param) throws Exception {
        if (!properties.isEnable()) {
            return false;
        }

        SmsTemplatesProperties templates = properties.getTemplates().get(templateCode);
        if (templates == null) {
            throw new MicroErrorException("Not Found Template");
        }
        if (param == null || param.length == 0) {
            throw new MicroErrorException("Sms template parameter is null or length is 0");
        }

        JSONObject jsonObject = new JSONObject();
        for (int i = 2; i < templates.getTemplateParams().size(); i++) {
            jsonObject.put(templates.getTemplateParams().get(i), param[i]);
        }
        String templateParam = jsonObject.toJSONString();
        log.info("Sms template param：{}", templateParam);

        SendSmsRequest request = new SendSmsRequest();
        request.setMethod(MethodType.POST);
        request.setPhoneNumbers(mobile);
        request.setSignName(templates.getSignName());
        request.setTemplateCode(templateCode);
        request.setTemplateParam(templateParam);
        SendSmsResponse sendSmsResponse = acsClient.getAcsResponse(request);
        if (sendSmsResponse.getCode() == null || !"OK".equals(sendSmsResponse.getCode())) {
            throw new MicroErrorException("Ali Yun send sms fail: " + JSON.toJSONString(sendSmsResponse));
        }

        return true;
    }

    @Override
    public void destroy() {
        if (acsClient != null) {
            acsClient.shutdown();
        }
    }

}
