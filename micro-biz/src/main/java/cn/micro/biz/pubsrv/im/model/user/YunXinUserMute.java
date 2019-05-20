package cn.micro.biz.pubsrv.im.model.user;

import lombok.Data;
import lombok.ToString;

import java.io.Serializable;

@Data
@ToString
public class YunXinUserMute implements Serializable {

    private String accid;
    /**
     * 是否全局禁言：true=全局禁言、false=取消全局禁言
     */
    private boolean mute;

}
