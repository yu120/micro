package cn.micro.biz.pubsrv.sms;

/**
 * 发送短信
 *
 * @author lry
 */
public interface ISmsService {

    /**
     * 发送短信
     *
     * @param smsSendConfig {@link SmsSendConfig}
     * @param smsSendParam  {@link SmsSendParam}
     * @return {@link SmsSendResult}
     * @throws Exception exception
     */
    SmsSendResult send(SmsSendConfig smsSendConfig, SmsSendParam smsSendParam) throws Exception;

}
