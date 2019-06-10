package cn.micro.biz.type.member;

import cn.micro.biz.commons.enums.IEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

/**
 * The Role Enum
 *
 * @author lry
 */
@Getter
@ToString
@AllArgsConstructor
public enum RoleEnum implements IEnum<Long> {

    // ======

    ROLE_MEMBER(0L, "基础用户"),
    ROLE_SUPER_ADMIN(1L, "超级管理员");

    private final Long value;
    private final String title;

}
