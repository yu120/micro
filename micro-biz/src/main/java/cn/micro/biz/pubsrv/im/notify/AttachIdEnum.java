package cn.micro.biz.pubsrv.im.notify;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum AttachIdEnum {

    // ====

    TEAM_INVITE("0", "即群拉人"),
    TEAM_KICK("1", "即群踢人"),
    TEAM_LEAVE("2", "即退出群"),
    TEAM_UPDATE("3", "即更新群信息"),
    TEAM_DISMISS("4", "即群解散"),

    TEAM_APPLY_PASS("5", "即群申请加入成功"),
    TEAM_OWNER_TRANSFER("6", "即群主退群并移交群主"),
    TEAM_ADD_MANAGER("7", "即增加管理员"),
    TEAM_REMOVE_MANAGER("8", "即删除管理员"),
    TEAM_INVITE_ACCEPT("9", "即群接受邀请进群");

    private final String value;
    private final String msg;

    public static AttachIdEnum get(String value) {
        if (value == null) {
            return null;
        }

        for (AttachIdEnum e : values()) {
            if (e.getValue().equals(value)) {
                return e;
            }
        }

        return null;

    }

}
