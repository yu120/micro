package cn.micro.biz.pubsrv.sms;

import org.apache.commons.collections.CollectionUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * SMS Service
 *
 * @author lry
 */
public class SmsService {

    private static final String KEY_TEMP = "%s:%s";
    private Map<String, List<SmsSendConfig>> smsSendConfigMap = new HashMap<>();

    public void send(SmsSendParam smsSendParam) {
        String key = String.format(KEY_TEMP, smsSendParam.getApp(), smsSendParam.getBiz());
        List<SmsSendConfig> smsSendConfigList = smsSendConfigMap.get(key);
        if (CollectionUtils.isEmpty(smsSendConfigList)) {
            return;
        }

        SmsSendConfig smsSendConfig = smsSendConfigList.get(0);
        
    }

}
