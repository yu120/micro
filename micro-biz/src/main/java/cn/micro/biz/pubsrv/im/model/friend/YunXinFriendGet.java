package cn.micro.biz.pubsrv.im.model.friend;

import lombok.Data;
import lombok.ToString;

import java.io.Serializable;

@Data
@ToString
public class YunXinFriendGet implements Serializable {

    private String accid;
    private Long updatetime;
    private Long createtime;

}
