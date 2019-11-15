package cn.micro.biz.pubsrv.event;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.io.Serializable;
import java.io.StringWriter;
import java.nio.charset.StandardCharsets;
import java.util.*;

/**
 * 异常告警
 *
 * @author lry
 */
@Slf4j
@Aspect
@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@EnableConfigurationProperties(ExceptionEventAlarmAdvice.AlarmProperties.class)
@ConditionalOnProperty(prefix = ExceptionEventAlarmAdvice.AlarmProperties.CACHE_PREFIX, name = "enable", havingValue = "true")
public class ExceptionEventAlarmAdvice implements InitializingBean {

    private ExceptionEventAlarm exceptionEventAlarm = new ExceptionEventAlarm();


    @Value("${spring.profiles.active}")
    private String active;
    @Value("${info.build.name}")
    private String application;
    private final AlarmProperties alarmProperties;

    @Override
    public void afterPropertiesSet() throws Exception {
        exceptionEventAlarm.initialize(alarmProperties.getSecret(), alarmProperties.getAccessToken(), null, null);
    }

    @RequestMapping(value = "api/alarm/event/without_token", method = RequestMethod.GET)
    public void exceptionInfo(@RequestParam("id") String id, HttpServletResponse response) throws Exception {
        ExceptionEventAlarm.EventInfo eventInfo = exceptionEventAlarm.getEventInfo(id);
        response.setCharacterEncoding(StandardCharsets.UTF_8.name());
        response.setContentType("text/html;charset=utf-8");
        response.getWriter().println(String.format(alarmProperties.getDocument(), eventInfo.getTitle(), eventInfo.getStackTrace()));
    }

    @Around("within(@org.springframework.web.bind.annotation.RestController *)")
    private Object around(ProceedingJoinPoint pjp) throws Throwable {
        try {
            return pjp.proceed();
        } catch (Throwable t) {
            String exceptionName = t.getClass().getSimpleName();
            String message = t.getMessage();

            ExceptionEventAlarm.EventInfo eventInfo = new ExceptionEventAlarm.EventInfo();
            eventInfo.setId(UUID.randomUUID().toString());
            eventInfo.setAt(null);
            eventInfo.setTitle(exceptionName);
            eventInfo.setMessage(message);
            eventInfo.setStackTrace(this.getStackTrace(t));
            exceptionEventAlarm.eventCollect(eventInfo);
            throw t;
        }
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

    @Data
    @ToString
    @ConfigurationProperties(prefix = AlarmProperties.CACHE_PREFIX)
    public static class AlarmProperties implements Serializable {

        public static final String CACHE_PREFIX = "micro.alarm";

        private boolean enable;
        private String secret = "SEC43de7f16e35ccbc6f7a8f085222f9cfdf1d975845d4b6a32e0925db6f1ca59c6";
        private String accessToken = "7fc60693d8ff1e1876d2dfb7d2a656c917a34d15901089e12406f21e63236079";
        private String uri = "http://api.test.sxw.cn/sxt/api/alarm/event/without_token?id=%s";
        private String document = "<!DOCTYPE html><html><head><title>%s</title>" +
                "<link href=\"https://prismjs.com/themes/prism.css\" rel=\"stylesheet\" />" +
                "<script src=\"https://prismjs.com/prism.js\"></script>" +
                "</head><body><pre><code class=\"language-css\">%s</code></pre></body></html>";
        private Map<String, String> developer = new HashMap<>();

    }

}
