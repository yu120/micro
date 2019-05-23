package cn.micro.biz.type.member;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Permission Category Enum
 *
 * @author lry
 */
@Getter
@AllArgsConstructor
public enum PermissionCategoryEnum {

    // ======

    TAB(1, "Tab"),
    MENU(2, "菜单"),
    ELEMENT(3, "页面元素"),
    LINK(4, "超链接");

    private final int value;
    private final String title;

}
