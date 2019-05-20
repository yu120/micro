package cn.micro.biz.pubsrv.im.model.team;

import lombok.Data;
import lombok.ToString;

import java.io.Serializable;
import java.util.List;

@Data
@ToString
public class YunXinTeamGetMarkReadInfoData implements Serializable {

    private Integer readSize;
    private Integer unreadSize;
    private List<String> readAccids;
    private List<String> unreadAccids;

}
