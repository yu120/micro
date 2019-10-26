package cn.micro.biz.pubsrv.sms;

import java.util.Date;

import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * SMS Service
 *
 * @author lry
 */
@Slf4j
public class SmsFactoryService {

    private static final String KEY_TEMP = "%s:%s";
    private Map<String, List<SmsSendConfig>> smsSendConfigMap = new HashMap<>();

    public void send(SmsSendParam smsSendParam) {
        String key = String.format(KEY_TEMP, smsSendParam.getApp(), smsSendParam.getBiz());
        List<SmsSendConfig> smsSendConfigList = smsSendConfigMap.get(key);
        if (CollectionUtils.isEmpty(smsSendConfigList)) {
            return;
        }

        SmsSendConfig smsSendConfig = smsSendConfigList.get(0);
        SmsSendLog smsSendLog = buildSmsSendLog(smsSendParam, smsSendConfig);

        ISmsService smsService = null;
        try {
            SmsSendResult smsSendResult = smsService.send(smsSendConfig, smsSendParam);
            smsSendLog.setSuccess(smsSendResult.getSuccess());
            smsSendLog.setResultJson(smsSendResult.getResultJson());
        } catch (Exception e) {
            smsSendLog.setSuccess(false);
            log.error(e.getMessage(), e);
        }

        log.info("SMS send:{}", smsSendLog);
    }

    private SmsSendLog buildSmsSendLog(SmsSendParam smsSendParam, SmsSendConfig smsSendConfig) {
        SmsSendLog smsSendLog = new SmsSendLog();
        smsSendLog.setPrepareTime(new Date());
        smsSendLog.setSendTime(new Date());
        smsSendLog.setApp(smsSendConfig.getApp());
        smsSendLog.setBiz(smsSendConfig.getBiz());
        smsSendLog.setCategory(smsSendConfig.getCategory());
        smsSendLog.setStore(smsSendConfig.getStore());
        smsSendLog.setSignName(smsSendConfig.getSignName());
        smsSendLog.setTemplateCode(smsSendConfig.getTemplateCode());
        smsSendLog.setMessage(smsSendConfig.getTemplateMessage());
        smsSendLog.setSyncSend(smsSendConfig.isSyncSend());
        smsSendLog.setDebug(smsSendConfig.isDebug());
        smsSendLog.setSendDingTalk(smsSendConfig.isSendDingTalk());
        smsSendLog.setDingTalkName(smsSendConfig.getDingTalkName());
        smsSendLog.setDingTalkNo(smsSendConfig.getDingTalkNo());
        smsSendLog.setIp(smsSendParam.getIp());
        smsSendLog.setTraceId(smsSendParam.getTraceId());
        smsSendLog.setMobile(smsSendParam.getMobile());
        smsSendLog.setParamsJson(JSON.toJSONString(smsSendParam.getParams()));
        return smsSendLog;
    }

}
