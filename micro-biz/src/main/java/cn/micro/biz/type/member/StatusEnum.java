package cn.micro.biz.type.member;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Public Status Category Enum
 *
 * @author lry
 */
@Getter
@AllArgsConstructor
public enum StatusEnum {

    // ======

    ENABLE(0, "Tab"),
    DISABLE(1, "菜单");

    private final int value;
    private final String title;

}
