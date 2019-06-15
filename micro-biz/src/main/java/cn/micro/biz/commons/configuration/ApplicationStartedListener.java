package cn.micro.biz.commons.configuration;

import org.springframework.beans.BeansException;
import org.springframework.boot.context.event.ApplicationStartedEvent;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;
import springfox.documentation.spring.web.DocumentationCache;

import javax.annotation.Nullable;

/**
 * Application Started Listener
 *
 * @author lry
 */
@Component
public class ApplicationStartedListener implements ApplicationContextAware, ApplicationListener<ApplicationStartedEvent> {

    private ApplicationContext applicationContext;

    @Override
    public void setApplicationContext(@Nullable ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    @Override
    public void onApplicationEvent(@Nullable ApplicationStartedEvent event) {
        DocumentationCache documentationCache = applicationContext.getBean(DocumentationCache.class);
        System.out.println("启动成功:" + documentationCache);
    }

}
