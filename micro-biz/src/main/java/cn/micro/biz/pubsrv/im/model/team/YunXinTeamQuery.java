package cn.micro.biz.pubsrv.im.model.team;

import lombok.Data;
import lombok.ToString;

import java.io.Serializable;

/**
 * YunXin Team Query
 *
 * @author lry
 */
@Data
@ToString
public class YunXinTeamQuery implements Serializable {

    private String tids;
    private int ope;

}
