package cn.micro.biz.pubsrv.event;

import cn.micro.biz.pubsrv.hook.DingTalkOutgoing;
import cn.micro.biz.pubsrv.hook.OutgoingResult;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.io.PrintWriter;
import java.io.StringWriter;

/**
 * Exception Event Alarm
 *
 * @author lry
 */
@Slf4j
@AllArgsConstructor
public class ExceptionEventAlarm {

    private String secret;
    private String accessToken;

    public static void main(String[] args) {
        ExceptionEventAlarm exceptionEventAlarm = new ExceptionEventAlarm(
                "SECe6439e9c8d5ddde21cc271f6c83f205f635bd8cb63bb2af893b6019a93d4ef80",
                "cf0cd4f757a3c5e0cba3e05ddd7edbe5212be0b14ad9ecf5990a934b83cd84c0");
        try {
            throw new RuntimeException("出错了");
        } catch (Exception e) {
            log.error("dddddd", e);
            exceptionEventAlarm.eventCollect(e);
        }
    }

    public void eventCollect(Throwable t) {
        String exceptionName = t.getClass().getSimpleName();
        String message = t.getMessage();
        String stackTrace = getStackTrace(t);

        DingTalkOutgoing.DingTalkOutgoingLink link = new DingTalkOutgoing.DingTalkOutgoingLink();
        link.setTitle(exceptionName);
        link.setText(message);
        link.setPicUrl("");
        link.setMessageUrl("http://www.baidu.com");

        DingTalkOutgoing.DingTalkOutgoingLinkRequest request = new DingTalkOutgoing.DingTalkOutgoingLinkRequest();
        request.setLink(link);
        request.setAt(new DingTalkOutgoing.DingTalkOutgoingAt(null, true));
        OutgoingResult outgoingResult = DingTalkOutgoing.push(secret, accessToken, request);
        log.info("The ding talk outgoing result:{}", outgoingResult);
    }

    private String getStackTrace(Throwable t) {
        StringWriter stringWriter = new StringWriter();
        try (PrintWriter printWriter = new PrintWriter(stringWriter);) {
            t.printStackTrace(printWriter);
        }

        return stringWriter.toString();
    }

}
