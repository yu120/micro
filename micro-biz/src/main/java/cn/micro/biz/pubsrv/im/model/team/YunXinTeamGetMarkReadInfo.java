package cn.micro.biz.pubsrv.im.model.team;

import lombok.Data;
import lombok.ToString;

import java.io.Serializable;

/**
 * YunXin Team Get Mark Read Info
 *
 * @author lry
 */
@Data
@ToString
public class YunXinTeamGetMarkReadInfo implements Serializable {

    private long tid;
    private long msgid;
    private String fromAccid;
    private boolean snapshot;

}
