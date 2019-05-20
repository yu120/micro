package org.micro.thread;

import lombok.extern.slf4j.Slf4j;
import org.micro.extension.Extension;
import org.micro.extension.ExtensionLoader;
import org.micro.extension.SPI;

import java.util.concurrent.*;

@Slf4j
public class ThreadPoolExecutorFactory {

    /**
     * The build executor
     *
     * @param threadType
     * @param prefixName
     * @param coreThread
     * @param maxThread
     * @param queues
     * @param keepAliveTime
     * @return
     */
    public static ThreadPoolExecutor buildExecutor(String threadType, String prefixName, int coreThread, int maxThread, int queues, long keepAliveTime) {
        return ExtensionLoader.getLoader(ThreadPool.class).getExtension(threadType).getExecutor(prefixName, coreThread, maxThread, queues, keepAliveTime);
    }

    /**
     * ThreadPool
     *
     * @author lry
     */
    @SPI
    public interface ThreadPool {
        ThreadPoolExecutor getExecutor(String prefixName, int coreThread, int maxThread, int queues, long keepAliveTime);
    }

    @Extension("cached")
    public static class CachedThreadPool implements ThreadPool {

        @Override
        public ThreadPoolExecutor getExecutor(String prefixName, int coreThread, int maxThread, int queues, long keepAliveTime) {
            BlockingQueue<Runnable> linkedBlockingQueue = queues < 0 ? new LinkedBlockingQueue<>() : new LinkedBlockingQueue<>(queues);
            BlockingQueue<Runnable> blockingQueue = queues == 0 ? new SynchronousQueue<>() : linkedBlockingQueue;
            return new ThreadPoolExecutor(coreThread, maxThread, keepAliveTime, TimeUnit.MILLISECONDS,
                    blockingQueue, new NamedThreadFactory(prefixName, true), new AbortPolicyWithReport(prefixName));
        }

    }


    /**
     * 此线程池启动时即创建固定大小的线程数，不做任何伸缩，来源于：<code>Executors.newFixedThreadPool()</code>
     *
     * @author lry
     * @see java.util.concurrent.Executors#newFixedThreadPool(int)
     */
    @Extension("fixed")
    public static class FixedThreadPool implements ThreadPool {

        @Override
        public ThreadPoolExecutor getExecutor(String prefixName, int coreThread, int maxThread, int queues, long keepAliveTime) {
            BlockingQueue<Runnable> linkedBlockingQueue = queues < 0 ? new LinkedBlockingQueue<>() : new LinkedBlockingQueue<>(queues);
            BlockingQueue<Runnable> blockingQueue = queues == 0 ? new SynchronousQueue<>() : linkedBlockingQueue;
            return new ThreadPoolExecutor(coreThread, coreThread, 0, TimeUnit.MILLISECONDS,
                    blockingQueue, new NamedThreadFactory(prefixName, true), new AbortPolicyWithReport(prefixName));
        }

    }

    /**
     * 此线程池一直增长，直到上限，增长后不收缩。
     *
     * @author lry
     */
    @Extension("limited")
    public static class LimitedThreadPool implements ThreadPool {

        @Override
        public ThreadPoolExecutor getExecutor(String prefixName, int coreThread, int maxThread, int queues, long keepAliveTime) {
            BlockingQueue<Runnable> linkedBlockingQueue = queues < 0 ? new LinkedBlockingQueue<>() : new LinkedBlockingQueue<>(queues);
            BlockingQueue<Runnable> blockingQueue = queues == 0 ? new SynchronousQueue<>() : linkedBlockingQueue;
            return new ThreadPoolExecutor(coreThread, maxThread, Long.MAX_VALUE, TimeUnit.MILLISECONDS,
                    blockingQueue, new NamedThreadFactory(prefixName, true), new AbortPolicyWithReport(prefixName));
        }

    }

    /**
     * Abort Policy.
     * Log warn info when abort.
     *
     * @author lry
     */
    public static class AbortPolicyWithReport extends ThreadPoolExecutor.AbortPolicy {

        private final String threadName;

        private AbortPolicyWithReport(String threadName) {
            this.threadName = threadName;
        }

        @Override
        public void rejectedExecution(Runnable r, ThreadPoolExecutor e) {
            String msg = String.format("Thread pool is EXHAUSTED!" +
                            " Thread Name: %s, Pool Size: %d (active: %d, core: %d, max: %d, largest: %d), Task: %d (completed: %d)," +
                            " Executor status:(isShutdown:%s, isTerminated:%s, isTerminating:%s)!",
                    threadName, e.getPoolSize(), e.getActiveCount(), e.getCorePoolSize(), e.getMaximumPoolSize(), e.getLargestPoolSize(),
                    e.getTaskCount(), e.getCompletedTaskCount(), e.isShutdown(), e.isTerminated(), e.isTerminating());
            log.warn(msg);
            throw new RejectedExecutionException(msg);
        }

    }

}
