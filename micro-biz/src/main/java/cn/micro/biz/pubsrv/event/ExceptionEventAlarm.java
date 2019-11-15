package cn.micro.biz.pubsrv.event;

import cn.micro.biz.pubsrv.hook.DingTalkOutgoing;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.google.common.util.concurrent.ThreadFactoryBuilder;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.*;
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

    private String secret;
    private String accessToken;
    private List<String> excludes = new ArrayList<>();
    private List<String> includes = new ArrayList<>();
    private Cache<String, AlarmEventInfo> cache;
    private ThreadPoolExecutor threadPoolExecutor;

    /**
     * The initialize
     *
     * @param secret      secret
     * @param accessToken access token
     * @param excludes    exception exclude list
     * @param includes    exception include list
     */
    public void initialize(String secret, String accessToken,
                           List<String> excludes,
                           List<String> includes) {
        this.secret = secret;
        this.accessToken = accessToken;
        if (excludes != null) {
            this.excludes.addAll(excludes);
        }
        if (includes != null) {
            this.includes.addAll(includes);
        }
        this.cache = CacheBuilder.newBuilder()
                .initialCapacity(20)
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
     * @param alarmEventInfo {@link AlarmEventInfo}
     */
    public void eventCollect(AlarmEventInfo alarmEventInfo) {
        try {
            String className = alarmEventInfo.getT().getClass().getName();
            if (!includes.isEmpty() && !includes.contains(className)) {
                return;
            }
            if (!excludes.isEmpty() && excludes.contains(className)) {
                return;
            }
            cache.put(alarmEventInfo.getId(), alarmEventInfo);

            // send alarm
            threadPoolExecutor.submit(() -> {
                try {
                    DingTalkOutgoing.DingTalkOutgoingMarkdown markdown = new DingTalkOutgoing.DingTalkOutgoingMarkdown();
                    markdown.setTitle(alarmEventInfo.getTitle());
                    markdown.setText(alarmEventInfo.getMessage());

                    DingTalkOutgoing.DingTalkOutgoingMarkdownRequest request = new DingTalkOutgoing.DingTalkOutgoingMarkdownRequest();
                    if (alarmEventInfo.getAt() != null && alarmEventInfo.getAt().length() > 0) {
                        markdown.setText("@" + alarmEventInfo.getAt() + "\n\n" + markdown.getText());
                        request.setAt(new DingTalkOutgoing.DingTalkOutgoingAt(
                                Collections.singletonList(alarmEventInfo.getAt()), false));
                    }
                    request.setMarkdown(markdown);
                    DingTalkOutgoing.push(secret, accessToken, request);
                } catch (Exception e) {
                    log.error("The event task collect exception", e);
                }
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
    public AlarmEventInfo getAlarmEventInfo(String key) {
        return cache.getIfPresent(key);
    }

    /**
     * The destroy
     */
    public void destroy() {
        if (threadPoolExecutor != null) {
            threadPoolExecutor.shutdown();
        }
    }

    /**
     * Alarm Event Info
     *
     * @author lry
     */
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class AlarmEventInfo {
        private String id;
        private String at;
        private String title;
        private String message;
        private String stackTrace;
        private Throwable t;
    }

}
