package cn.micro.biz.pubsrv.im.model.team;

import lombok.Data;
import lombok.ToString;

import java.io.Serializable;

/**
 * YunXin Team Change Owner
 *
 * @author lry
 */
@Data
@ToString
public class YunXinTeamChangeOwner implements Serializable {

    private String tid;
    private String owner;
    private String member;
    private int leave;

}
