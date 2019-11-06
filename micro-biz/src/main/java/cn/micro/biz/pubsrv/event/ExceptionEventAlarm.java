package cn.micro.biz.pubsrv.event;


import cn.micro.biz.pubsrv.hook.DingTalkOutgoing;
import cn.micro.biz.pubsrv.hook.OutgoingResult;

/**
 * Exception Event Alarm
 *
 * @author lry
 */
public class ExceptionEventAlarm {

    public void eventCollect(Throwable t) {
        DingTalkOutgoing.DingTalkOutgoingTextRequest dingTalkOutgoingTextRequest = new DingTalkOutgoing.DingTalkOutgoingTextRequest();
        dingTalkOutgoingTextRequest.setText(new DingTalkOutgoing.DingTalkOutgoingText("测试机器人功能的消息201905"));
        dingTalkOutgoingTextRequest.setAt(new DingTalkOutgoing.DingTalkOutgoingAt(null, true));
        OutgoingResult outgoingResult = DingTalkOutgoing.push(null,
                "0044bea6737e89921d27495e5d57592ccd10a74ab04a4b39b1ec7ff87db6106c", dingTalkOutgoingTextRequest);
    }

}
