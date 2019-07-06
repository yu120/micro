package cn.micro.biz.pubsrv.im.model.team;

import lombok.Data;
import lombok.ToString;

import java.io.Serializable;

/**
 * YunXin Team Mute Team
 *
 * @author lry
 */
@Data
@ToString
public class YunXinTeamMuteTeam implements Serializable {

    private String tid;
    private String accid;
    private Integer ope;

}
