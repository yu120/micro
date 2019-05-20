package cn.micro.biz.pubsrv.im.model.friend;

import lombok.Data;
import lombok.ToString;

import java.io.Serializable;

@Data
@ToString
public class YunXinUserSetSpecialRelation implements Serializable {

    private String accid;
    private String targetAcc;
    private int relationType;
    private int value;

}
