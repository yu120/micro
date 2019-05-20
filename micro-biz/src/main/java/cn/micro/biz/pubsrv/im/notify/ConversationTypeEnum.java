package cn.micro.biz.pubsrv.im.notify;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 会话具体类型
 *
 * @author lry
 */
@Getter
@AllArgsConstructor
public enum ConversationTypeEnum {

    // ====

    PERSON("PERSON", "二人会话数据"),
    TEAM("TEAM", "群聊数据"),
    CUSTOM_PERSON("CUSTOM_PERSON", "个人自定义系统通知"),
    CUSTOM_TEAM("CUSTOM_TEAM", "群组自定义系统通知");

    private final String value;
    private final String msg;

    public static ConversationTypeEnum get(String value) {
        if (value == null) {
            return null;
        }

        for (ConversationTypeEnum e : values()) {
            if (e.getValue().equals(value)) {
                return e;
            }
        }

        return null;
    }
}
