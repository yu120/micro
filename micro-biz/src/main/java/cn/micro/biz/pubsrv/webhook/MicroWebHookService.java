package cn.micro.biz.pubsrv.webhook;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * Micro Web Hook Properties
 *
 * @author lry
 */
@Slf4j
@Getter
@Configuration
@EnableConfigurationProperties(MicroWebHookProperties.class)
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class MicroWebHookService implements InitializingBean {

    private final MicroWebHookProperties properties;

    private IWebHook dingTalkWebHook;
    private IWebHook bearyChatWebHook;

    @Override
    public void afterPropertiesSet() {
        this.dingTalkWebHook = new DingTalkWebHook(properties.getDingTalkAccessToken());
        this.bearyChatWebHook = new DingTalkWebHook(properties.getBearyChatAccessToken());
    }

}
