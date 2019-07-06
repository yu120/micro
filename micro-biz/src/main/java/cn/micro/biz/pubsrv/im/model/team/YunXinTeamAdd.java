package cn.micro.biz.pubsrv.im.model.team;

import lombok.Data;
import lombok.ToString;

import java.io.Serializable;

/**
 * YunXin Team Add
 *
 * @author lry
 */
@Data
@ToString
public class YunXinTeamAdd implements Serializable {

    private String tid;
    private String owner;
    private String members;
    private Integer magree;
    private String msg;
    private String attach;

}
