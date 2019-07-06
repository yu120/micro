package cn.micro.biz.pubsrv.im;

import cn.micro.biz.pubsrv.im.notify.YunXinNotifyEvent;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.lmax.disruptor.EventHandler;
import com.lmax.disruptor.RingBuffer;
import com.lmax.disruptor.YieldingWaitStrategy;
import com.lmax.disruptor.dsl.Disruptor;
import com.lmax.disruptor.dsl.ProducerType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * YunXin Notify Service
 *
 * @author lry
 */
@Slf4j
@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class YunXinNotifyService implements InitializingBean, DisposableBean {

    private final YunXinProperties yunXinProperties;

    private ThreadPoolExecutor threadPoolExecutor;
    private Disruptor<YunXinNotifyEvent> disruptor;
    private Cache<String, YunXinNotifyEvent> failEventCache;
    private ScheduledExecutorService scheduledExecutorService;

    @Override
    public void afterPropertiesSet() {
        // Step 1: 创建缓存和缓存处理线程
        this.failEventCache = CacheBuilder.newBuilder()
                .initialCapacity(yunXinProperties.getFailCacheMaximumSize() / 2)
                .concurrencyLevel(yunXinProperties.getFailCacheConcurrencyLevel())
                .maximumSize(yunXinProperties.getFailCacheMaximumSize())
                .expireAfterWrite(yunXinProperties.getFailCacheExpireAfterWriteSec(), TimeUnit.SECONDS).build();

        // Step 2: 周期性处理失败的事件
        this.scheduledExecutorService = Executors.newSingleThreadScheduledExecutor();
        this.scheduledExecutorService.scheduleAtFixedRate(() -> {
            ConcurrentMap<String, YunXinNotifyEvent> tempMap = failEventCache.asMap();
            if (tempMap != null) {
                for (Map.Entry<String, YunXinNotifyEvent> entry : tempMap.entrySet()) {
                    try {
                        this.onEvent(entry.getValue());
                    } catch (Exception e) {
                        log.error(e.getMessage(), e);
                    }
                }
            }
        }, yunXinProperties.getFailPeriodSec(), yunXinProperties.getFailPeriodSec(), TimeUnit.SECONDS);

        // Step 3: 队列满了丢任务不异常
        this.threadPoolExecutor = new ThreadPoolExecutor(
                yunXinProperties.getEventThreadNum(), yunXinProperties.getEventThreadNum(),
                0L, TimeUnit.MILLISECONDS, new LinkedBlockingQueue<>(),
                new ThreadFactory() {
                    private AtomicInteger threadIndex = new AtomicInteger(0);

                    @Override
                    public Thread newThread(Runnable r) {
                        Thread thread = new Thread(r);
                        thread.setName("yun-xin-notify-event-" + (threadIndex.incrementAndGet()));
                        thread.setDaemon(true);
                        return thread;
                    }
                }, new ThreadPoolExecutor.DiscardPolicy());

        // Step 4: create disruptor
        this.disruptor = new Disruptor<>(YunXinNotifyEvent::new, yunXinProperties.getEventRingBufferSize(),
                threadPoolExecutor, ProducerType.SINGLE, new YieldingWaitStrategy());

        // Step 5: add handle event
        this.disruptor.handleEventsWith((EventHandler<YunXinNotifyEvent>) (yunXinNotifyEvent, sequence, endOfBatch) -> {
            try {
                // 将父线程的MDC内容传给子线程
                MDC.setContextMap(yunXinNotifyEvent.getContextMap());
                RequestContextHolder.setRequestAttributes(yunXinNotifyEvent.getRequestAttributes());
                log.debug("正在处理事件：{}", yunXinNotifyEvent.toString());
                this.onEvent(yunXinNotifyEvent);
            } catch (Exception e) {
                // 收集异常事件
                failEventCache.put(yunXinNotifyEvent.getId(), yunXinNotifyEvent);
                log.error(e.getMessage(), e);
            } finally {
                // 清空MDC内容
                MDC.clear();
                RequestContextHolder.resetRequestAttributes();
            }
        });

        // Step 6: starr disruptor
        this.disruptor.start();
    }

    private void onEvent(YunXinNotifyEvent yunXinNotifyEvent) {
        // TODO
    }

    /**
     * 发布事件
     *
     * @param yunXinNotifyEvent {@link YunXinNotifyEvent}
     */
    public void publish(final YunXinNotifyEvent yunXinNotifyEvent) {
        try {
            RingBuffer<YunXinNotifyEvent> ringBuffer = disruptor.getRingBuffer();
            long sequence = ringBuffer.next();
            try {
                YunXinNotifyEvent tempYunXinNotifyEvent = ringBuffer.get(sequence);
                tempYunXinNotifyEvent.setId(yunXinNotifyEvent.getId());
                tempYunXinNotifyEvent.setMemberId(yunXinNotifyEvent.getMemberId());
                tempYunXinNotifyEvent.setContextMap(new HashMap<>(MDC.getCopyOfContextMap()));
                tempYunXinNotifyEvent.setRequestAttributes(RequestContextHolder.getRequestAttributes());
            } finally {
                ringBuffer.publish(sequence);
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
    }

    @Override
    public void destroy() {
        if (disruptor != null) {
            disruptor.shutdown();
        }
        if (threadPoolExecutor != null) {
            threadPoolExecutor.shutdown();
        }
        if (scheduledExecutorService != null) {
            scheduledExecutorService.shutdown();
        }
    }

}
