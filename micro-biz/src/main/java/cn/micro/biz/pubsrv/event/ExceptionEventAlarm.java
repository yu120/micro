package cn.micro.biz.pubsrv.event;

import cn.micro.biz.pubsrv.hook.DingTalkOutgoing;
import com.alibaba.fastjson.JSON;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.google.common.util.concurrent.ThreadFactoryBuilder;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.io.PrintWriter;
import java.io.StringWriter;
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
    private Cache<String, EventInfo> cache;
    private ThreadPoolExecutor threadPoolExecutor;

    public static void main(String[] args) {
        ExceptionEventAlarm exceptionEventAlarm = new ExceptionEventAlarm();
        exceptionEventAlarm.initialize(
                "SECe6439e9c8d5ddde21cc271f6c83f205f635bd8cb63bb2af893b6019a93d4ef80",
                "cf0cd4f757a3c5e0cba3e05ddd7edbe5212be0b14ad9ecf5990a934b83cd84c0",
                null, null);
        try {
            throw new RuntimeException("出错了");
        } catch (Exception e) {
            EventInfo eventInfo = new EventInfo();
            eventInfo.setId(UUID.randomUUID().toString());
            eventInfo.setAt("15828252029");
            eventInfo.setTitle("测试");
            eventInfo.setMessage("的点点滴滴");
            eventInfo.setStackTrace("22222");
            exceptionEventAlarm.eventCollect(eventInfo);
            log.error("dddddd", e);
        }
    }

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
        if (excludes != null && excludes.size() > 0) {
            this.excludes.addAll(excludes);
        }
        if (includes != null && includes.size() > 0) {
            this.includes.addAll(includes);
        }
        this.cache = CacheBuilder.newBuilder()
                .initialCapacity(20)
                .concurrencyLevel(5)
                .maximumSize(150)
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
     * @param eventInfo {@link EventInfo}
     */
    public void eventCollect(EventInfo eventInfo) {
        try {
            cache.put(eventInfo.getId(), eventInfo);

            // send alarm
            threadPoolExecutor.submit(() -> {
                try {
                    DingTalkOutgoing.DingTalkOutgoingMarkdown markdown = new DingTalkOutgoing.DingTalkOutgoingMarkdown();
                    markdown.setTitle(eventInfo.getTitle());
                    markdown.setText(eventInfo.getMessage());

                    DingTalkOutgoing.DingTalkOutgoingMarkdownRequest request = new DingTalkOutgoing.DingTalkOutgoingMarkdownRequest();
                    if (eventInfo.getAt() != null && eventInfo.getAt().length() > 0) {
                        markdown.setText(markdown.getText() + "\n@" + eventInfo.getAt());
                        request.setAt(new DingTalkOutgoing.DingTalkOutgoingAt(Collections.singletonList(eventInfo.getAt()), false));
                    } else {
                        request.setAt(new DingTalkOutgoing.DingTalkOutgoingAt(null, true));
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
    public EventInfo getEventInfo(String key) {
        return cache.getIfPresent(key);
    }

    /**
     * Exception Event Info
     *
     * @author lry
     */
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class EventInfo {
        private String id;
        private String at;
        private String title;
        private String message;
        private String stackTrace;
    }

}
