package cn.micro.biz.pubsrv.im.model.team;

import lombok.Data;
import lombok.ToString;

import java.io.Serializable;

/**
 * YunXin Team Remove
 *
 * @author lry
 */
@Data
@ToString
public class YunXinTeamRemove implements Serializable {

    private String tid;
    private String owner;

}
