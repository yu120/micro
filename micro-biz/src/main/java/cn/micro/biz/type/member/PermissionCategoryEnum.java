package cn.micro.biz.type.member;

import cn.micro.biz.commons.enums.IEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

/**
 * Permission Category Enum
 *
 * @author lry
 */
@Getter
@ToString
@AllArgsConstructor
public enum PermissionCategoryEnum implements IEnum<Integer> {

    // ======

    TAB(0, "Tab"),
    MENU(1, "菜单"),
    ELEMENT(2, "页面元素"),
    LINK(3, "超链接");

    private final Integer value;
    private final String title;

}
