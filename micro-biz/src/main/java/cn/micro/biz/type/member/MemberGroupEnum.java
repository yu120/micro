package cn.micro.biz.type.member;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Member Group Enum
 *
 * @author lry
 */
@Getter
@AllArgsConstructor
public enum MemberGroupEnum {

    // ======,

    MEMBER(0L, "普通成员"),
    SYS_SUPER_ADMIN(1L, "系统超级管理员"),
    SYS_ADMIN(2L, "系统管理员"),
    TENANT_SUPER_ADMIN(3L, "租户超级管理员"),
    TENANT_ADMIN(4L, "租户管理员");

    private final long value;
    private final String title;

}
