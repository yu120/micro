package cn.micro.biz.pubsrv.im.model.history;

import lombok.Data;
import lombok.ToString;

import java.io.Serializable;

/**
 * YunXin History Query Session Msg
 *
 * @author lry
 */
@Data
@ToString
public class YunXinHistoryQuerySessionMsg implements Serializable {

    private String from;
    private String to;
    private String begintime;
    private String endtime;
    private int limit;
    private int reverse;
    private String type;

}
