package cn.micro.biz.pubsrv.im.model.team;

import lombok.Data;
import lombok.ToString;

import java.io.Serializable;

@Data
@ToString
public class YunXinTeamCreate implements Serializable {

    private String tid;
    private String tname;
    private String owner;
    private String members;

    private String announcement;
    private String intro;

    private String msg;
    private Integer magree;
    private Integer joinmode;

    private String custom;
    private String icon;
    private Integer beinvitemode;
    private Integer invitemode;
    private Integer uptinfomode;
    private Integer upcustommode;
    private Integer teamMemberLimit;

}
