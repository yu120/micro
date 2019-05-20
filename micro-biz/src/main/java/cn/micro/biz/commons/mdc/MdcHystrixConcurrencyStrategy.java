package cn.micro.biz.commons.mdc;

import com.netflix.hystrix.strategy.concurrency.HystrixConcurrencyStrategy;
import org.slf4j.MDC;
import org.springframework.web.context.request.RequestContextHolder;

import java.util.concurrent.Callable;

/**
 * Hystrix线程池隔离支持日志链路跟踪
 * <p>
 * 使用方式：在hystrix-plugins.properties中增加：
 * <code>
 * hystrix.plugin.HystrixConcurrencyStrategy.implementation=cn.micro.biz.commons.mdc.MdcHystrixConcurrencyStrategy
 * </code>
 * 同时还可以自定义：
 * 1.HystrixEventNotifier: 可以监听这些事件进行一些告警和数据收集
 * 2.HystrixConcurrencyStrategy: 并发策略
 * 3.HystrixMetricsPublisher: 可以获取所有实例的metrics信息
 * 4.HystrixPropertiesStrategy: 提供Hystrix配置信息
 * 5.HystrixCommandExecutionHook: 可以在HystrixCommand或HystrixObservableCommand执行期间的响应阶段进行回调
 * <p>
 * 另一种方式：HystrixRequestContext
 *
 * @author lry
 */
public class MdcHystrixConcurrencyStrategy extends HystrixConcurrencyStrategy {

    @Override
    public <T> Callable<T> wrapCallable(Callable<T> callable) {
        return new MdcAwareCallable<>(callable, MDC.getCopyOfContextMap(), RequestContextHolder.getRequestAttributes());
    }

}