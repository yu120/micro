package cn.micro.biz.pubsrv.event;

import com.google.common.eventbus.AsyncEventBus;
import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import com.google.common.eventbus.SubscriberExceptionHandler;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Event Collect Factory
 *
 * @author lry
 */
@Slf4j
@Getter
public class EventCollectFactory {

    private SubscriberExceptionHandler exceptionLoggerHandler;
    private ThreadPoolExecutor threadPoolExecutor;
    private EventBus eventBus;
    private AsyncEventBus asyncEventBus;
    private final List<Object> eventBusBean = new ArrayList<>();

    public EventCollectFactory() {
        int coreThread = Runtime.getRuntime().availableProcessors();
        initialize(2 * coreThread, 2 * coreThread, 10 * coreThread);
    }

    public EventCollectFactory(int coreThread, int maxThread, int queueSize) {
        initialize(coreThread, maxThread, queueSize);
    }

    /**
     * The initialize event collect factory
     *
     * @param coreThread core thread
     * @param maxThread  max thread
     * @param queueSize  queue size
     */
    private void initialize(int coreThread, int maxThread, int queueSize) {
        exceptionLoggerHandler = (exception, context) -> {
            Method method = context.getSubscriberMethod();
            String message = String.format("Exception thrown by subscriber method %s(%s) " +
                            "on subscriber %s when dispatching event: %s",
                    method.getName(),
                    method.getParameterTypes()[0].getName(),
                    context.getSubscriber(),
                    context.getEvent());
            log.error(message, exception);
        };

        // 队列满了丢任务不异常
        threadPoolExecutor = new ThreadPoolExecutor(
                coreThread, maxThread, 0L, TimeUnit.MILLISECONDS,
                new LinkedBlockingQueue<>(queueSize),
                new ThreadFactory() {
                    private AtomicInteger threadIndex = new AtomicInteger(0);

                    @Override
                    public Thread newThread(Runnable r) {
                        Thread thread = new Thread(r);
                        thread.setName("event-collect-" + (threadIndex.incrementAndGet()));
                        thread.setDaemon(true);
                        return thread;
                    }
                }, new ThreadPoolExecutor.DiscardPolicy());

        this.eventBus = new EventBus(exceptionLoggerHandler);
        this.asyncEventBus = new AsyncEventBus(threadPoolExecutor, exceptionLoggerHandler);
    }

    /**
     * The register event bus
     *
     * @param object object
     */
    public void register(Object object) {
        Method[] methods = object.getClass().getDeclaredMethods();
        if (methods.length == 0) {
            return;
        }

        for (Method method : methods) {
            Subscribe subscribe = method.getAnnotation(Subscribe.class);
            if (subscribe == null) {
                continue;
            }

            eventBus.register(object);
            asyncEventBus.register(object);
            eventBusBean.add(object);
            return;
        }
    }

    /**
     * The register event bus list
     *
     * @param objects object list
     */
    public void registers(List<Object> objects) {
        for (Object object : objects) {
            register(object);
        }
    }

    /**
     * The publish aop event
     *
     * @param microEvent {@link MicroEvent}
     * @param className  class name
     * @param methodName method name
     * @param args       arg list
     * @param result     result
     */
    public void publish(MicroEvent microEvent, String className, String methodName, Object[] args, Object result) {
        if (!microEvent.enable()) {
            return;
        }

        List<Object> data = new ArrayList<>();
        if (args != null) {
            Collections.addAll(data, args);
        }

        EventCollect event = new EventCollect();
        event.setClassName(className);
        event.setMethodName(methodName);
        event.setArgs(data);
        event.setResult(result);
        event.setTitle(microEvent.value());
        event.setDesc(microEvent.desc());

        publish(microEvent.enable(), microEvent.async(), event);
    }

    /**
     * The publish event
     *
     * @param enable enable
     * @param async  async
     * @param object object
     */
    public void publish(boolean enable, boolean async, Object object) {
        if (enable) {
            if (async) {
                asyncEventBus.post(object);
            } else {
                eventBus.post(object);
            }
        }
    }

    /**
     * The unregister event bus
     *
     * @param object object
     */
    public void unregister(Object object) {
        eventBus.unregister(object);
        asyncEventBus.unregister(object);
    }

    /**
     * The unregister event bus list
     *
     * @param objects object list
     */
    public void unregisters(List<Object> objects) {
        for (Object bean : eventBusBean) {
            this.unregister(bean);
        }
    }

    /**
     * The unregister all event bus list
     */
    public void unregisterAll() {
        for (Object bean : eventBusBean) {
            this.unregister(bean);
        }
    }

    /**
     * The destroy event collect factory
     */
    public void destroy() {
        unregisterAll();
        if (threadPoolExecutor != null) {
            threadPoolExecutor.shutdown();
        }
    }

}
