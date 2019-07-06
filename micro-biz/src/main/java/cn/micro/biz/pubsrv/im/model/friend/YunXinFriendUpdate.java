package cn.micro.biz.pubsrv.im.model.friend;

import lombok.Data;
import lombok.ToString;

import java.io.Serializable;

/**
 * YunXin Friend Update
 *
 * @author lry
 */
@Data
@ToString
public class YunXinFriendUpdate implements Serializable {

    private String accid;
    private String faccid;
    private String alias;
    private String ex;
    private String serverex;

}
