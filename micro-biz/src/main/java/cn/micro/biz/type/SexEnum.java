package cn.micro.biz.type;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;
import java.util.List;

/**
 * 用户性别
 *
 * @author lry
 */
@Getter
@AllArgsConstructor
public enum SexEnum {

    // ======

    UNKNOWN(0, "未知"),
    MEN(1, "男"),
    WOMEN(2, "女");

    public static final List<Integer> GROUP = Arrays.asList(0, 1, 2);

    private final int value;
    private final String title;

}
