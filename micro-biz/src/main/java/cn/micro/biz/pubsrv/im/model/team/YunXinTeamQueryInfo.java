package cn.micro.biz.pubsrv.im.model.team;

import lombok.Data;
import lombok.ToString;

import java.io.Serializable;
import java.util.List;

/**
 * YunXin Team Query Info
 *
 * @author lry
 */
@Data
@ToString
public class YunXinTeamQueryInfo implements Serializable {

    private String tname;
    private String announcement;
    private String owner;
    private Integer maxusers;
    private Integer joinmode;
    private Long tid;
    private String intro;
    private Integer size;
    private String custom;
    private String clientCustom;
    private boolean mute;
    private Long createtime;
    private Long updatetime;
    private List<String> admins;
    private List<String> members;

}
