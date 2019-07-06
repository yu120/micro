package cn.micro.biz.pubsrv.im.model.team;

import lombok.Data;
import lombok.ToString;

import java.io.Serializable;

/**
 * YunXin Team Remove Manager
 *
 * @author lry
 */
@Data
@ToString
public class YunXinTeamRemoveManager implements Serializable {

    private String tid;
    private String owner;
    private String members;

}
