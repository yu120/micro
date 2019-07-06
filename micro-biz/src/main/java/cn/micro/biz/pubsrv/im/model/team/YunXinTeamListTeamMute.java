package cn.micro.biz.pubsrv.im.model.team;

import lombok.Data;
import lombok.ToString;

import java.io.Serializable;

/**
 * YunXin Team List Team Mute
 *
 * @author lry
 */
@Data
@ToString
public class YunXinTeamListTeamMute implements Serializable {

    private String tid;
    private String owner;

}
