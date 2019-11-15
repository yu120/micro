package cn.micro.biz.pubsrv.event;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.DisposableBean;
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
 * Exception Event Alarm Advice
 *
 * @author lry
 */
@Slf4j
@Aspect
@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@EnableConfigurationProperties(ExceptionEventAlarmAdvice.AlarmProperties.class)
@ConditionalOnProperty(prefix = ExceptionEventAlarmAdvice.CACHE_PREFIX, name = "enable", havingValue = "true")
public class ExceptionEventAlarmAdvice implements InitializingBean, DisposableBean {

    public static final String CACHE_PREFIX = "micro.alarm";
    private ExceptionEventAlarm exceptionEventAlarm = new ExceptionEventAlarm();

    @Value("${spring.profiles.active}")
    private String active;
    @Value("${spring.profiles.application}")
    private String application;

    private final AlarmProperties alarmProperties;

    @Override
    public void afterPropertiesSet() throws Exception {
        exceptionEventAlarm.initialize(alarmProperties.getSecret(), alarmProperties.getAccessToken(), null, null);
    }

    @RequestMapping(value = "alarm/event", method = RequestMethod.GET)
    public void exceptionInfo(@RequestParam("id") String id, HttpServletResponse response) throws Exception {
        ExceptionEventAlarm.AlarmEventInfo info = exceptionEventAlarm.getAlarmEventInfo(id);
        response.setCharacterEncoding(StandardCharsets.UTF_8.name());
        response.setContentType("text/html;charset=utf-8");
        response.getWriter().println(String.format(alarmProperties.getDocument(), info.getTitle(), info.getStackTrace()));
    }

    @Around("within(@org.springframework.web.bind.annotation.RestController *)")
    private Object around(ProceedingJoinPoint pjp) throws Throwable {
        try {
            return pjp.proceed();
        } catch (Throwable t) {
            String exceptionName = t.getClass().getSimpleName();
            String message = t.getMessage();

            ExceptionEventAlarm.AlarmEventInfo alarmEventInfo = new ExceptionEventAlarm.AlarmEventInfo();
            alarmEventInfo.setId(UUID.randomUUID().toString());
            alarmEventInfo.setAt(null);
            alarmEventInfo.setTitle(exceptionName);
            alarmEventInfo.setMessage(message);
            alarmEventInfo.setStackTrace(this.getStackTrace(t));
            alarmEventInfo.setT(t);
            exceptionEventAlarm.eventCollect(alarmEventInfo);
            throw t;
        }
    }

    @Override
    public void destroy() throws Exception {
        exceptionEventAlarm.destroy();
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
    @ConfigurationProperties(prefix = ExceptionEventAlarmAdvice.CACHE_PREFIX)
    public static class AlarmProperties implements Serializable {

        private boolean enable;
        private String secret ;
        private String accessToken;

        private Map<String, String> developer = new HashMap<>();
        private String uri = "http://localhost:7777/alarm/event?id=%s";
        private String document = "<!DOCTYPE html><html><head><title>%s</title>" +
                "<link href=\"https://prismjs.com/themes/prism.css\" rel=\"stylesheet\" />" +
                "<script src=\"https://prismjs.com/prism.js\"></script>" +
                "</head><body><pre><code class=\"language-css\">%s</code></pre></body></html>";

    }

}
