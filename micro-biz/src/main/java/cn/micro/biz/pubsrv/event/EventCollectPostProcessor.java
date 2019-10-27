package cn.micro.biz.pubsrv.event;

import com.google.common.eventbus.Subscribe;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.Nullable;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 * Event Collect Post Processor
 *
 * @author lry
 */
@Configuration
public class EventCollectPostProcessor implements BeanPostProcessor, DisposableBean {

    private EventCollectFactory asyncEventFactory;
    private final List<Object> eventBusBean = new ArrayList<>();

    @Bean
    public EventCollectFactory asyncEventFactory() {
        this.asyncEventFactory = new EventCollectFactory();
        asyncEventFactory.initialize();
        return asyncEventFactory;
    }

    @Override
    public Object postProcessBeforeInitialization(Object bean, @Nullable String beanName) throws BeansException {
        Method[] methods = bean.getClass().getDeclaredMethods();
        if (methods.length == 0) {
            return bean;
        }

        for (Method method : methods) {
            Subscribe subscribe = method.getAnnotation(Subscribe.class);
            if (subscribe == null) {
                continue;
            }

            asyncEventFactory.getEventBus().register(bean);
            asyncEventFactory.getAsyncEventBus().register(bean);

            eventBusBean.add(bean);
        }

        return bean;
    }

    @Override
    public void destroy() throws Exception {
        if (asyncEventFactory != null) {
            asyncEventFactory.destroy();
            for (Object bean : eventBusBean) {
                asyncEventFactory.getEventBus().unregister(bean);
                asyncEventFactory.getAsyncEventBus().unregister(bean);
            }
        }
    }

}