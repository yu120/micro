package cn.micro.biz.pubsrv.event;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Event Collect Post Processor
 *
 * @author lry
 */
@Configuration
public class EventCollectPostProcessor implements BeanPostProcessor, DisposableBean {

    private EventCollectFactory asyncEventFactory;

    @Bean
    public EventCollectFactory asyncEventFactory() {
        return this.asyncEventFactory = new EventCollectFactory();
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