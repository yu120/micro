package cn.micro.biz.pubsrv.im.notify.msg;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 会话具体类型CUSTOM_TEAM对应的通知消息类型：
 *
 * @author lry
 */
@Getter
@AllArgsConstructor
public enum CustomTeamMsgTypeEnum {

    // ====

    TEAM_CREATE("TEAM_CREATE", "创建群"),
    TEAM_APPLY("TEAM_APPLY", "申请入群"),
    TEAM_APPLY_REJECT("TEAM_APPLY_REJECT", "拒绝入群申请"),
    TEAM_INVITE("TEAM_INVITE", "邀请进群"),

    TEAM_INVITE_REJECT("TEAM_INVITE_REJECT", "拒绝邀请"),
    TLIST_UPDATE("TLIST_UPDATE", "群信息更新"),
    CUSTOM_TEAM_MSG("CUSTOM_TEAM_MSG", "群组自定义系统通知");

    private final String value;
    private final String msg;

    public static CustomTeamMsgTypeEnum get(String value) {
        if (value == null) {
            return null;
        }

        for (CustomTeamMsgTypeEnum e : values()) {
            if (e.getValue().equals(value)) {
                return e;
            }
        }

        return null;
    }

}
