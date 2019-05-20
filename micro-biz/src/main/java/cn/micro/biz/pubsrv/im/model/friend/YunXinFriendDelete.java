package cn.micro.biz.pubsrv.im.model.friend;

import lombok.Data;
import lombok.ToString;

import java.io.Serializable;

@Data
@ToString
public class YunXinFriendDelete implements Serializable {

    private String accid;
    private String faccid;
    private boolean isDeleteAlias = true;

}
