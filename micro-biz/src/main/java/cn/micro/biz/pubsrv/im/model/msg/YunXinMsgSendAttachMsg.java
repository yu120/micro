package cn.micro.biz.pubsrv.im.model.msg;

import lombok.Data;
import lombok.ToString;

import java.io.Serializable;

/**
 * YunXin Msg Send Attach Msg
 *
 * @author lry
 */
@Data
@ToString
public class YunXinMsgSendAttachMsg implements Serializable {

    private String from;
    private int msgtype;
    private String to;
    private String attach;
    private String pushcontent;
    private String payload;
    private String sound;
    private int save;
    private String option;

}
