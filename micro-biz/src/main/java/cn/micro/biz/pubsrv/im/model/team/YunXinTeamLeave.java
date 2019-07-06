package cn.micro.biz.pubsrv.im.model.team;

import lombok.Data;
import lombok.ToString;

import java.io.Serializable;

/**
 * YunXin Team Leave
 *
 * @author lry
 */
@Data
@ToString
public class YunXinTeamLeave implements Serializable {

    private String tid;
    private String accid;

}
