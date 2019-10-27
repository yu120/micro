package cn.micro.biz.pubsrv.event;

import com.google.common.eventbus.AsyncEventBus;
import com.google.common.eventbus.EventBus;
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

    /**
     * The initialize event collect factory
     */
    public void initialize() {
        int coreThread = Runtime.getRuntime().availableProcessors();
        initialize(2 * coreThread, 2 * coreThread, 10 * coreThread);
    }

    /**
     * The initialize event collect factory
     *
     * @param coreThread core thread
     * @param maxThread  max thread
     * @param queueSize  queue size
     */
    public void initialize(int coreThread, int maxThread, int queueSize) {
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
                        thread.setName("event-task-" + (threadIndex.incrementAndGet()));
                        thread.setDaemon(true);
                        return thread;
                    }
                }, new ThreadPoolExecutor.DiscardPolicy());

        this.eventBus = new EventBus(exceptionLoggerHandler);
        this.asyncEventBus = new AsyncEventBus(threadPoolExecutor, exceptionLoggerHandler);
    }

    /**
     * The  publish event
     *
     * @param guavaEvent {@link MicroEvent}
     * @param className
     * @param methodName
     * @param args
     * @param result
     */
    public void publishEvent(MicroEvent guavaEvent, String className, String methodName, Object[] args, Object result) {
        List<Object> data = new ArrayList<>();
        if (args != null) {
            Collections.addAll(data, args);
        }

        EventCollect event = new EventCollect();
        event.setClassName(className);
        event.setMethodName(methodName);
        event.setArgs(data);
        event.setResult(result);
        event.setTitle(guavaEvent.value());
        event.setDesc(guavaEvent.desc());

        if (guavaEvent.async()) {
            asyncEventBus.post(event);
        } else {
            eventBus.post(event);
        }
    }

    /**
     * The destroy event collect factory
     */
    public void destroy() {
        if (threadPoolExecutor != null) {
            threadPoolExecutor.shutdown();
        }
    }

}
