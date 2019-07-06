package cn.micro.biz.pubsrv.im.model.team;

import lombok.Data;
import lombok.ToString;

import java.io.Serializable;

/**
 * YunXin Team Mute Team List All
 * @author lry
 */
@Data
@ToString
public class YunXinTeamMuteTlistAll implements Serializable {

    private String tid;
    private String owner;
    private String mute;
    private Integer muteType;

}
