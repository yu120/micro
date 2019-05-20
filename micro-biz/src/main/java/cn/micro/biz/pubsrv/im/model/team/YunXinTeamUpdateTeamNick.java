package cn.micro.biz.pubsrv.im.model.team;

import lombok.Data;
import lombok.ToString;

import java.io.Serializable;

@Data
@ToString
public class YunXinTeamUpdateTeamNick implements Serializable {

    private String tid;
    private String owner;
    private String accid;
    private String nick;
    private String custom;

}
