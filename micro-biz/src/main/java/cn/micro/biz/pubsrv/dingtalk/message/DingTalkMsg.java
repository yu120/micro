package cn.micro.biz.pubsrv.dingtalk.message;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * Ding Talk Msg
 *
 * @author lry
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DingTalkMsg implements Serializable {

    @JSONField(name = "msgtype")
    private String msgtype;

    @JSONField(name = "action_card")
    private DingTalkActionCard actionCard;
    @JSONField(name = "file")
    private DingTalkFile file;
    @JSONField(name = "image")
    private DingTalkImage image;
    @JSONField(name = "link")
    private DingTalkLink link;
    @JSONField(name = "markdown")
    private DingTalkMarkdown markdown;
    @JSONField(name = "oa")
    private DingTalkOA oa;
    @JSONField(name = "text")
    private DingTalkText text;
    @JSONField(name = "voice")
    private DingTalkVoice voice;

    public void setMsgType(DingTalkMsgType msgType) {
        this.msgtype = msgType.getCode();
    }

}
