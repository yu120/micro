package cn.micro.biz.pubsrv.im.model.team;

import lombok.Data;
import lombok.ToString;

import java.io.Serializable;

/**
 * YunXin Team Kick
 *
 * @author lry
 */
@Data
@ToString
public class YunXinTeamKick implements Serializable {

    private String tid;
    private String owner;
    private String members;
    private String attach;

}
