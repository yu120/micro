package cn.micro.biz.pubsrv.event.spring;

import cn.micro.biz.pubsrv.event.EventCollectFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.context.annotation.Configuration;

/**
 * Event Collect Post Processor
 *
 * @author lry
 */
@Configuration
public class MicroEventCollectPostProcessor implements InitializingBean, BeanPostProcessor, DisposableBean {

    private EventCollectFactory asyncEventFactory;

    @Override
    public void afterPropertiesSet() throws Exception {
        this.asyncEventFactory = new EventCollectFactory();
    }

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        if (asyncEventFactory != null) {
            asyncEventFactory.register(bean);
        }

        return bean;
    }

    @Override
    public void destroy() throws Exception {
        if (asyncEventFactory != null) {
            asyncEventFactory.destroy();
        }
    }

}