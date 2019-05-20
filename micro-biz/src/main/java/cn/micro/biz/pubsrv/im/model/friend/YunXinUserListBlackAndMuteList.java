package cn.micro.biz.pubsrv.im.model.friend;

import lombok.Data;
import lombok.ToString;

import java.io.Serializable;
import java.util.List;

@Data
@ToString
public class YunXinUserListBlackAndMuteList implements Serializable {

    private List<String> mutelist;
    private List<String> blacklist;

}
