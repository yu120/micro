package cn.micro.biz.pubsrv.im.model.team;

import lombok.Data;
import lombok.ToString;

import java.io.Serializable;

@Data
@ToString
public class YunXinTeamMuteTlistAll implements Serializable {

    private String tid;
    private String owner;
    private String mute;
    private Integer muteType;

}
