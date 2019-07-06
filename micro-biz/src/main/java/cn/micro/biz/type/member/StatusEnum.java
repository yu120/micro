package cn.micro.biz.type.member;

import cn.micro.biz.commons.enums.IEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

/**
 * Status Enable Category Enum
 *
 * @author lry
 */
@Getter
@ToString
@AllArgsConstructor
public enum StatusEnum implements IEnum<Integer> {

    // ======

    ENABLE(0, "启用"),
    DISABLE(1, "禁用");

    private final Integer value;
    private final String title;

}
