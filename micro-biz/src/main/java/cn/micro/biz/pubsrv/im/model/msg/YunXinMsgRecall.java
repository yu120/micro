package cn.micro.biz.pubsrv.im.model.msg;

import lombok.Data;
import lombok.ToString;

import java.io.Serializable;

/**
 * YunXin Msg Recall
 *
 * @author lry
 */
@Data
@ToString
public class YunXinMsgRecall implements Serializable {

    private String deleteMsgid;
    private String timetag;
    private int type;
    private String from;
    private String to;
    private String msg;
    private String ignoreTime;
    private String pushcontent;
    private String payload;

}
