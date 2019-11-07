package cn.micro.biz.pubsrv.event;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletResponse;

/**
 * Exception Event Configuration
 *
 * @author lry
 */
@Slf4j
@Controller
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class ExceptionEventConfiguration implements InitializingBean {

    private ExceptionEventAlarm exceptionEventAlarm;

    @Bean
    public ExceptionEventAlarm exceptionEventAlarm() {
        exceptionEventAlarm = new ExceptionEventAlarm();
        exceptionEventAlarm.initialize(
                "SECe6439e9c8d5ddde21cc271f6c83f205f635bd8cb63bb2af893b6019a93d4ef80",
                "cf0cd4f757a3c5e0cba3e05ddd7edbe5212be0b14ad9ecf5990a934b83cd84c0",
                "http://localhost:7777/event-info?eventKey=%s", null, null);
        return exceptionEventAlarm;
    }

    @Override
    public void afterPropertiesSet() throws Exception {

    }

    @RequestMapping(value = "event-info", method = RequestMethod.GET)
    public void getEventInfo(@RequestParam("eventKey") String eventKey, HttpServletResponse response) throws Exception {
        ExceptionEventAlarm.ExceptionEventInfo exceptionEventInfo = exceptionEventAlarm.getEventInfo(eventKey);
        response.getWriter().println(exceptionEventInfo.getStackTrace());
    }

}
