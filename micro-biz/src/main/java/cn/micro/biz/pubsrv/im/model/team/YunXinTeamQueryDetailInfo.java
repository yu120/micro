package cn.micro.biz.pubsrv.im.model.team;

import lombok.Data;
import lombok.ToString;

import java.io.Serializable;
import java.util.List;

@Data
@ToString
public class YunXinTeamQueryDetailInfo implements Serializable {

    private String icon;
    private String announcement;
    private String uptinfomode;
    private Integer maxusers;
    private String intro;
    private String upcustommode;
    private String tname;
    private String beinvitemode;
    private String joinmode;
    private String tid;
    private String invitemode;
    private boolean mute;
    private String custom;
    private String clientCustom;
    private Long createtime;
    private Long updatetime;

    private YunXinTeamQueryMemberDetailInfo owner;
    private List<YunXinTeamQueryMemberDetailInfo> admins;
    private List<YunXinTeamQueryMemberDetailInfo> members;

    @Data
    @ToString
    public static class YunXinTeamQueryMemberDetailInfo implements Serializable {
        private Long createtime;
        private Long updatetime;
        private String nick;
        private String accid;
        private boolean mute;
        private String custom;
    }

}
