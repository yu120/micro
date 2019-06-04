package cn.micro.biz.commons.trace;

import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.tuple.Pair;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

/**
 * Trace Stack Context
 *
 * @author lry
 */
@Slf4j
public class TraceStackContext {

    static final List<String> SKIP_URLS = new ArrayList<>();

    static {
        SKIP_URLS.add("/swagger-resources/**");
        SKIP_URLS.add("/swagger-ui.html");
        SKIP_URLS.add("/v2/api-docs");
        SKIP_URLS.add("/webjars/**");
        SKIP_URLS.add("/index.html");
        SKIP_URLS.add("/csrf");
        SKIP_URLS.add("/error");
        SKIP_URLS.add("/static/**");
        SKIP_URLS.add("/favicon.ico");
    }

    private static final SimpleDateFormat SDF_SHORT = new SimpleDateFormat("HH:mm:ss.SSS");
    /**
     * 调用堆栈信息记录，每次在start和stop时，都进行清理，避免出现引用仍然存在的问题
     */
    private static final InheritableThreadLocal<StackTrace> STACK_TRACE = new InheritableThreadLocal<>();
    /**
     * 线程统计开始标记，每次在start和stop时，都进行清理，避免出现引用仍然存在的问题
     */
    private static final InheritableThreadLocal<Boolean> IS_START = new InheritableThreadLocal<>();
    /**
     * 调用响应耗时(ms)统计，超过该值响应的请求会被输出到日志中
     */
    private static TraceProperties properties;

    public static void initialize(TraceProperties properties) {
        TraceStackContext.properties = properties;
    }

    /**
     * 开始统计当前线程的执行延时
     */
    public static void start(String message) {
        // 避免异常情况未清理，造成内存泄露
        STACK_TRACE.remove();
        // 创建当前线程的调用堆栈实例，用于记录关联路径的调用情况，关键路径通过全局的interceptor进行拦截
        STACK_TRACE.set(new StackTrace(message));
        // 开始标记
        IS_START.set(Boolean.TRUE);
    }

    /**
     * 结束统计，返回整个调用的耗时
     */
    public static Long stop() {
        if (STACK_TRACE.get() == null) {
            return null;
        }

        // 记录线程执行结束时间
        STACK_TRACE.get().end();
        // 记录延时，返回监控profile信息的filter
        long duration = STACK_TRACE.get().duration();
        //结束标记
        IS_START.remove();
        //清理 threadLocal 引用
        STACK_TRACE.remove();

        return duration;
    }

    public static String getDump() {
        if (STACK_TRACE.get() == null) {
            return null;
        }

        return STACK_TRACE.get().dump();
    }

    /**
     * 结束统计, 整个调用的耗时，并打印结果日志
     */
    public static Pair<String, Long> stopAndPrint() {
        if (STACK_TRACE.get() == null) {
            return null;
        }

        // 记录线程执行结束时间
        STACK_TRACE.get().end();
        // 记录延时，返回监控profile信息的filter
        long duration = STACK_TRACE.get().duration();
        // dump调用堆栈
        String dumpStack = STACK_TRACE.get().dump();
        //结束标记
        IS_START.remove();
        //清理 threadLocal 引用
        STACK_TRACE.remove();

        return Pair.of(dumpStack, duration);
    }

    /**
     * 方法进入时记录线程的执行堆栈信息，拦截器调用
     */
    public static void enter(String signature, List<Object> args) {
        //如果已经标记为开始，并且 stacktrace实例已经创建，则进行统计
        if ((IS_START.get() != null) && (STACK_TRACE.get() != null)) {
            STACK_TRACE.get().enter(new Entry(signature, args));
        }
    }

    /**
     * 方法return时记录线程的执行堆栈信息，拦截器调用
     */
    public static void exit(Object ret, Integer exception, String stack) {
        //如果已经标记为开始，并且 stacktrace实例已经创建，则进行统计
        if ((IS_START.get() != null) && (STACK_TRACE.get() != null)) {
            STACK_TRACE.get().exit(ret, exception, stack);
        }
    }

    /**
     * 当前线程的线程的堆栈信息
     *
     * @author lry
     */
    public static final class StackTrace {
        private List<Entry> entryList = new ArrayList<>();
        private Stack<Entry> entryStack = new Stack<>();

        /**
         * 当前线程的描述信息，timer filter调用时为请求的url
         */
        private String message;
        private long beginTime;
        private long endTime;

        /**
         * 当前处于堆栈的层次
         */
        private int currentStackLevel;

        /**
         * 线程执行开始
         */
        StackTrace(String message) {
            this.beginTime = System.currentTimeMillis();
            this.endTime = beginTime;
            this.message = message;
            this.currentStackLevel = 0;
        }

        /**
         * 记录方法的进入
         */
        void enter(Entry entry) {
            entry.level = ++this.currentStackLevel;
            entryList.add(entry);
            entryStack.push(entry);
        }

        /**
         * 记录方法的返回
         */
        void exit(Object ret, Integer exception, String stack) {
            this.currentStackLevel--;
            Entry entryPop = entryStack.pop();
            entryPop.ret = ret;
            entryPop.exception = exception;
            entryPop.stack = stack;
            entryPop.exitTimestamp = System.currentTimeMillis();
        }

        /**
         * 线程执行结束
         */
        void end() {
            endTime = System.currentTimeMillis();
        }

        /**
         * 线程执行延时
         */
        long duration() {
            return endTime - beginTime;
        }

        String dump() {
            long dur = this.duration();
            StringBuilder sb = new StringBuilder();
            sb.append("Total Delay [").append(duration()).append("ms] ").append("===> ").append(this.message).append("\n");

            List<Entry> entryLogList = new ArrayList<>(entryList);
            for (Entry entry : entryLogList) {
                if (entry == null) {
                    continue;
                }
                for (int i = 0; i < entry.level; i++) {
                    sb.append("    ");
                }
                sb.append(entry.toString()).append("\n");
            }

            // 大于阈值才需要warn
            if (TraceStackContext.properties.getThreshold().toMillis() <= 0L) {
                log.info("Response time [{}ms], stack info:\n[{}]", this.duration(), sb.toString());
            } else if (dur > TraceStackContext.properties.getThreshold().toMillis()) {
                log.warn("Response time [{}ms] exceed thresholdMillis [{}ms], stack info:\n[{}]",
                        this.duration(), TraceStackContext.properties.getThreshold().toMillis(), sb.toString());
            } else {
                log.debug("Response time [{}ms], stack info:\n[{}]", this.duration(), sb.toString());
            }

            return sb.toString();
        }
    }

    public static final class Entry {
        private String signature;

        private List<Object> args;
        private Object ret;

        private int level;

        private Integer exception;
        private String stack;

        private long enterTimestamp;
        private long exitTimestamp;

        Entry(String signature, List<Object> args) {
            this.enterTimestamp = System.currentTimeMillis();
            this.exitTimestamp = this.enterTimestamp;
            this.signature = signature;
            this.args = args;
            this.level = 0;
        }

        @Override
        public String toString() {
            StringBuilder sb = new StringBuilder("+---");

            sb.append("[").append(this.exitTimestamp - this.enterTimestamp)
                    .append("ms][").append(signature).append("]");
            sb.append("[").append(SDF_SHORT.format(this.enterTimestamp))
                    .append("->").append(SDF_SHORT.format(this.exitTimestamp)).append("]");
            if (properties.isPrintArgs()) {
                String argsMsg = (this.args == null) ? null : JSON.toJSONString(this.args);
                if (argsMsg != null) {
                    sb.append("\n");
                    for (int i = 0; i < level; i++) {
                        sb.append("    ");
                    }
                    sb.append("    [IN]:").append(argsMsg);
                }
                String retMsg = (this.args == null) ? null : JSON.toJSONString(this.ret);
                if (retMsg != null) {
                    sb.append("\n");
                    for (int i = 0; i < level; i++) {
                        sb.append("    ");
                    }
                    sb.append("    [OUT]:").append(retMsg);
                }
            }

            if (!(stack == null || stack.length() == 0)) {
                sb.append("\n");
                for (int i = 0; i < level; i++) {
                    sb.append("    ");
                }
                sb.append("    [Stack]:").append(stack).append("]");
            }

            return sb.toString();
        }

    }

}
