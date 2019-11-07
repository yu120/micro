package cn.micro.biz.pubsrv.event;

import cn.micro.biz.commons.utils.RsaUtils;
import cn.micro.biz.pubsrv.hook.DingTalkOutgoing;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.google.common.util.concurrent.ThreadFactoryBuilder;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * Exception Event Alarm
 *
 * @author lry
 */
@Slf4j
public class ExceptionEventAlarm {

    private static final int MAX_TEXT = 100;

    private String secret;
    private String accessToken;
    private String uri;
    private List<String> excludes = new ArrayList<>();
    private List<String> includes = new ArrayList<>();
    private Cache<String, ExceptionEventInfo> cache;
    private ThreadPoolExecutor threadPoolExecutor;

    public static void main(String[] args) {
        ExceptionEventAlarm exceptionEventAlarm = new ExceptionEventAlarm();
        exceptionEventAlarm.initialize(
                "SECe6439e9c8d5ddde21cc271f6c83f205f635bd8cb63bb2af893b6019a93d4ef80",
                "cf0cd4f757a3c5e0cba3e05ddd7edbe5212be0b14ad9ecf5990a934b83cd84c0",
                "http://www.baidu.com?id=%s", null, null);
        try {
            throw new RuntimeException("出错了");
        } catch (Exception e) {
            log.error("dddddd", e);
            exceptionEventAlarm.eventCollect(e);
        }
    }

    /**
     * The initialize
     *
     * @param secret      secret
     * @param accessToken access token
     * @param uri         uri
     * @param excludes    exception exclude list
     * @param includes    exception include list
     */
    public void initialize(String secret, String accessToken, String uri,
                           List<Class<? extends Throwable>> excludes,
                           List<Class<? extends Throwable>> includes) {
        this.secret = secret;
        this.accessToken = accessToken;
        this.uri = uri;
        if (excludes != null && excludes.size() > 0) {
            for (Class<? extends Throwable> clazz : excludes) {
                this.excludes.add(clazz.getName());
            }
        }
        if (includes != null && includes.size() > 0) {
            for (Class<? extends Throwable> clazz : includes) {
                this.includes.add(clazz.getName());
            }
        }
        this.cache = CacheBuilder.newBuilder()
                .initialCapacity(100)
                .concurrencyLevel(5)
                .maximumSize(100)
                .expireAfterWrite(30, TimeUnit.MINUTES)
                .build();
        ThreadFactoryBuilder builder = new ThreadFactoryBuilder();
        builder.setDaemon(true);
        builder.setNameFormat("exception-event-alarm");
        this.threadPoolExecutor = new ThreadPoolExecutor(1, 5,
                0L, TimeUnit.MILLISECONDS, new LinkedBlockingQueue<>(), builder.build());
    }

    /**
     * The event collect
     *
     * @param t {@link Throwable}
     */
    public void eventCollect(Throwable t) {
        try {
            String className = t.getClass().getName();
            if (includes.size() > 0 && !includes.contains(className)) {
                return;
            }
            if (excludes.size() > 0 && excludes.contains(className)) {
                return;
            }

            String exceptionName = t.getClass().getSimpleName();
            String message = t.getMessage();
            String stackTrace = getStackTrace(t);
            String eventKey = RsaUtils.encryptPassword(exceptionName + message);
            ExceptionEventInfo exceptionEventInfo = new ExceptionEventInfo(exceptionName, message, stackTrace);
            cache.put(eventKey, exceptionEventInfo);

            // send alarm
            threadPoolExecutor.submit(() -> {
                DingTalkOutgoing.DingTalkOutgoingLink link = new DingTalkOutgoing.DingTalkOutgoingLink();
                link.setTitle(exceptionName);
                link.setText(message.length() < MAX_TEXT ? message : message.substring(0, MAX_TEXT));
                link.setMessageUrl(String.format(uri, eventKey));
                DingTalkOutgoing.DingTalkOutgoingLinkRequest request = new DingTalkOutgoing.DingTalkOutgoingLinkRequest();
                request.setLink(link);
                request.setAt(new DingTalkOutgoing.DingTalkOutgoingAt(null, true));
                DingTalkOutgoing.push(secret, accessToken, request);
            });
        } catch (Exception e) {
            log.error("The event collect exception", e);
        }
    }

    /**
     * The event info
     *
     * @param key event key
     */
    public ExceptionEventInfo getEventInfo(String key) {
        return cache.getIfPresent(key);
    }

    /**
     * The get stack trace
     *
     * @param t {@link Throwable}
     * @return stack trace
     */
    private String getStackTrace(Throwable t) {
        StringWriter stringWriter = new StringWriter();
        try (PrintWriter printWriter = new PrintWriter(stringWriter);) {
            t.printStackTrace(printWriter);
        }

        return stringWriter.toString();
    }

    /**
     * Exception Event Info
     *
     * @author lry
     */
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ExceptionEventInfo {
        private String exceptionName;
        private String message;
        private String stackTrace;
    }

}
