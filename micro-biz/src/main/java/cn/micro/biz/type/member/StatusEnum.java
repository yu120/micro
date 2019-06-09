package cn.micro.biz.type.member;

import cn.micro.biz.commons.enums.IEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

/**
 * Public Status Category Enum
 *
 * @author lry
 */
@Getter
@ToString
@AllArgsConstructor
public enum StatusEnum implements IEnum<Integer> {

    // ======

    ENABLE(0, "Tab"),
    DISABLE(1, "菜单");

    private final Integer value;
    private final String title;

}
