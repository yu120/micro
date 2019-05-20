package cn.micro.biz.pubsrv.im.model.friend;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class YunXinFriend extends YunXinFriendAdd {

    private boolean bidirection;
    private String createtime;

}
