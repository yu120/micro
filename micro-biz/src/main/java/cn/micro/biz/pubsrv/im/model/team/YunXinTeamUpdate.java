package cn.micro.biz.pubsrv.im.model.team;

import lombok.Data;
import lombok.ToString;

import java.io.Serializable;

@Data
@ToString
public class YunXinTeamUpdate implements Serializable {

    private String tid;
    private String tname;
    private String owner;

    private String announcement;
    private String intro;

    private Integer joinmode;

    private String custom;
    private String icon;
    private Integer beinvitemode;
    private Integer invitemode;
    private Integer uptinfomode;
    private Integer upcustommode;
    private Integer teamMemberLimit;

}
