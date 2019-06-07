package cn.micro.biz.type.member;

import cn.micro.biz.commons.enums.IEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

/**
 * Member Status Category Enum
 *
 * @author lry
 */
@Getter
@ToString
@AllArgsConstructor
public enum MemberStatusEnum implements IEnum<Integer> {

    // ======

    NORMAL(0, "正常"),
    FORBIDDEN(1, "禁用");

    private final Integer value;
    private final String title;

}
