package cn.micro.biz.pubsrv.im.model.friend;

import lombok.Data;
import lombok.ToString;

import java.io.Serializable;

/**
 * YunXin Friend Add
 *
 * @author lry
 */
@Data
@ToString
public class YunXinFriendAdd implements Serializable {

    private String accid;
    private String faccid;
    private int type;
    private String msg;
    private String serverex;

}
