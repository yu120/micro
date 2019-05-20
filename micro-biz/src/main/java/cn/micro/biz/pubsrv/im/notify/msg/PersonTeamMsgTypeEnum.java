package cn.micro.biz.pubsrv.im.notify.msg;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 会话具体类型PERSON、TEAM对应的通知消息类型
 *
 * @author lry
 */
@Getter
@AllArgsConstructor
public enum PersonTeamMsgTypeEnum {

    // ====

    TEXT("TEXT", ""),
    PICTURE("PICTURE", ""),
    AUDIO("AUDIO", ""),
    VIDEO("VIDEO", ""),

    LOCATION("LOCATION", ""),
    NOTIFICATION("NOTIFICATION", ""),
    FILE("FILE", "文件消息"),
    NETCALL_AUDIO("NETCALL_AUDIO", "网络电话音频聊天"),

    NETCALL_VEDIO("NETCALL_VEDIO", "网络电话视频聊天"),
    DATATUNNEL_NEW("DATATUNNEL_NEW", "新的数据通道请求通知"),
    TIPS("TIPS", "提示类型消息"),
    CUSTOM("CUSTOM", "自定义消息");

    private final String value;
    private final String msg;

    public static PersonTeamMsgTypeEnum get(String value) {
        if (value == null) {
            return null;
        }

        for (PersonTeamMsgTypeEnum e : values()) {
            if (e.getValue().equals(value)) {
                return e;
            }
        }

        return null;
    }

}
