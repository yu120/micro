package cn.micro.biz.pubsrv.im.notify.msg;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 会话具体类型CUSTOM_PERSON对应的通知消息类型：
 *
 * @author lry
 */
@Getter
@AllArgsConstructor
public enum CustomPersonMsgTypeEnum {

    // ====

    FRIEND_ADD("FRIEND_ADD", "加好友"),
    FRIEND_DELETE("FRIEND_DELETE", "删除好友"),
    CUSTOM_P2P_MSG("CUSTOM_P2P_MSG", "个人自定义系统通知");

    private final String value;
    private final String msg;

    public static CustomPersonMsgTypeEnum get(String value) {
        if (value == null) {
            return null;
        }

        for (CustomPersonMsgTypeEnum e : values()) {
            if (e.getValue().equals(value)) {
                return e;
            }
        }

        return null;
    }

}
