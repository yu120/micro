package cn.micro.biz.pubsrv.event;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 事件采集
 * <p>
 * eg:行为收集
 *
 * @author lry
 */
@Slf4j
@Configuration
@EnableConfigurationProperties(MicroEventProperties.class)
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@ConditionalOnProperty(prefix = MicroEventProperties.PREFIX, name = "enable", havingValue = "true")
public class MicroEventService implements InitializingBean, DisposableBean {

    private static ThreadPoolExecutor threadPoolExecutor;
    private static boolean enable;

    private final MicroEventProperties properties;

    @Override
    public void afterPropertiesSet() {
        enable = properties.isEnable();
        if (!enable) {
            return;
        }

        // 队列满了丢任务不异常
        threadPoolExecutor = new ThreadPoolExecutor(
                properties.getCoreThread(),
                properties.getMaxThread(),
                0L, TimeUnit.MILLISECONDS,
                new LinkedBlockingQueue<>(properties.getQueueSize()),
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
    }

    public static void collect(MicroEventAction microEventAction) {
        if (!enable) {
            return;
        }

        try {
            threadPoolExecutor.submit(() -> {
                log.info("{}", microEventAction);
            });
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
    }

    @Override
    public void destroy() {
        if (threadPoolExecutor != null) {
            threadPoolExecutor.shutdown();
        }
    }

}
