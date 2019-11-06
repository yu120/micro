package cn.micro.biz.pubsrv.sms;

import java.util.Date;

import cn.micro.biz.pubsrv.hook.DingTalkOutgoing;
import cn.micro.biz.pubsrv.hook.OutgoingResult;
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
    private static final String SMS_PARAM_TEMP = "${%s}";
    private static final String SMS_MESSAGE_TEMP = "接收人:%s\n%s";

    private Map<String, List<SmsSendConfig>> smsSendConfigMap = new HashMap<>();

    public void send(SmsSendParam smsSendParam) {
        String key = String.format(KEY_TEMP, smsSendParam.getApp(), smsSendParam.getBiz());
        List<SmsSendConfig> smsSendConfigList = smsSendConfigMap.get(key);
        if (CollectionUtils.isEmpty(smsSendConfigList)) {
            return;
        }

        SmsSendConfig smsSendConfig = loadBalance(smsSendConfigList);
        SmsSendLog smsSendLog = buildSmsSendLog(smsSendParam, smsSendConfig);
        if (smsSendConfig.getSyncSend()) {
            // 同步发送
            syncSend(smsSendParam, smsSendConfig, smsSendLog);
        } else {
            // 异步发送
            syncSend(smsSendParam, smsSendConfig, smsSendLog);
        }
    }

    /**
     * 同步发短信
     *
     * @param smsSendParam  {@link SmsSendParam}
     * @param smsSendConfig {@link SmsSendConfig}
     * @param smsSendLog    {@link SmsSendLog}
     */
    private void syncSend(SmsSendParam smsSendParam, SmsSendConfig smsSendConfig, SmsSendLog smsSendLog) {
        // 需要发送短信
        if (!smsSendConfig.getDebug()) {
            ISmsService smsService = null;
            try {
                SmsSendResult smsSendResult = smsService.send(smsSendConfig, smsSendParam);
                smsSendLog.setSuccess(smsSendResult.getSuccess());
                smsSendLog.setMsg(smsSendResult.getMsg());
                smsSendLog.setPlain(smsSendResult.getPlain());
            } catch (Exception e) {
                smsSendLog.setSuccess(false);
                smsSendLog.setMsg(e.getMessage());
                log.error(e.getMessage(), e);
            }
            log.info("Send sms:{}", smsSendLog);
        }

        // 需要发送钉钉
        if (smsSendConfig.getSendDingTalk()) {
            // 拼接短信内容
            String message = smsSendConfig.getTemplateMessage();
            if (smsSendParam.getParams() != null) {
                for (Map.Entry<String, Object> entry : smsSendParam.getParams().entrySet()) {
                    String paramKey = String.format(SMS_PARAM_TEMP, entry.getKey());
                    if (message.contains(paramKey)) {
                        message = message.replace(paramKey, String.valueOf(entry.getValue()));
                    }
                }
            }
            message = String.format(SMS_MESSAGE_TEMP, smsSendParam.getMobile(), message);

            DingTalkOutgoing.DingTalkOutgoingTextRequest request = new DingTalkOutgoing.DingTalkOutgoingTextRequest();
            request.setText(new DingTalkOutgoing.DingTalkOutgoingText(message));
            OutgoingResult webHookResult = DingTalkOutgoing.push(
                    smsSendConfig.getDingTalkSecret(), smsSendConfig.getDingTalkAccessToken(), request);
            log.info("Send Ding Talk:{}", webHookResult);
        }
    }

    /**
     * 短信服务商和模板负载切换
     *
     * @param smsSendConfigList {@link List<SmsSendConfig>}
     * @return {@link SmsSendConfig}
     */
    private SmsSendConfig loadBalance(List<SmsSendConfig> smsSendConfigList) {
        return smsSendConfigList.get(0);
    }

    /**
     * The build SMS send log
     *
     * @param smsSendParam  {@link SmsSendParam}
     * @param smsSendConfig {@link SmsSendConfig}
     * @return {@link SmsSendLog}
     */
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
        smsSendLog.setSyncSend(smsSendConfig.getSyncSend());
        smsSendLog.setDebug(smsSendConfig.getDebug());
        smsSendLog.setSendDingTalk(smsSendConfig.getSendDingTalk());
        smsSendLog.setDingTalkName(smsSendConfig.getDingTalkName());
        smsSendLog.setDingTalkNo(smsSendConfig.getDingTalkNo());
        smsSendLog.setIp(smsSendParam.getIp());
        smsSendLog.setTraceId(smsSendParam.getTraceId());
        smsSendLog.setMobile(smsSendParam.getMobile());
        smsSendLog.setParamsJson(JSON.toJSONString(smsSendParam.getParams()));
        return smsSendLog;
    }

}
