package cn.micro.biz.type.member;

import cn.micro.biz.commons.enums.IEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

import java.util.Arrays;
import java.util.List;

/**
 * 用户性别
 *
 * @author lry
 */
@Getter
@ToString
@AllArgsConstructor
public enum SexEnum implements IEnum<Integer> {

    // ======

    UNKNOWN(0, "未知"),
    MEN(1, "男"),
    WOMEN(2, "女");

    public static final List<Integer> GROUP = Arrays.asList(0, 1, 2);

    private final Integer value;
    private final String title;

}
