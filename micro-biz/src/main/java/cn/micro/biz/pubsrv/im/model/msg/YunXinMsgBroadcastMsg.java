package cn.micro.biz.pubsrv.im.model.msg;

import lombok.Data;
import lombok.ToString;

import java.io.Serializable;

/**
 * YunXin Msg Broadcast Msg
 *
 * @author lry
 */
@Data
@ToString
public class YunXinMsgBroadcastMsg implements Serializable {

    private String body;
    private String from;
    private String isOffline;
    private int ttl;
    private String targetOs;

}
