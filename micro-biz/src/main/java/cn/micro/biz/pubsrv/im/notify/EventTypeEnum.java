package cn.micro.biz.pubsrv.im.notify;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Event Type Enum
 *
 * @author lry
 */
@Getter
@AllArgsConstructor
public enum EventTypeEnum {

    // ====

    CONVERSATION("1", "表示CONVERSATION消息，即会话类型的消息（目前包括P2P聊天消息，群组聊天消息，群组操作，好友操作）"),
    LOGIN("2", "表示LOGIN消息，即用户登录事件的消息"),
    LOGOUT("3", "表示LOGOUT消息，即用户登出事件的消息"),
    ;

    private final String value;
    private final String msg;

    public static EventTypeEnum get(String value) {
        if (value == null) {
            return null;
        }

        for (EventTypeEnum e : values()) {
            if (e.getValue().equals(value)) {
                return e;
            }
        }

        return null;
    }
}
