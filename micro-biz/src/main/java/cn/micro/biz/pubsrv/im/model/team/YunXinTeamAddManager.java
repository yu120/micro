package cn.micro.biz.pubsrv.im.model.team;

import lombok.Data;
import lombok.ToString;

import java.io.Serializable;

/**
 * YunXin Team Add Manager
 *
 * @author lry
 */
@Data
@ToString
public class YunXinTeamAddManager implements Serializable {

    private String tid;
    private String owner;
    private String members;

}
