package cn.micro.biz.type.member;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum RoleEnum {

    // ======

    ROLE_MEMBER(0, "基础用户"),
    ROLE_SUPER_ADMIN(1, "超级管理员");

    private final long value;
    private final String title;

}
