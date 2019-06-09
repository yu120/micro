package cn.micro.biz.type.unified;

import cn.micro.biz.commons.enums.IEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Login Category
 *
 * @author lry
 */
@Getter
@AllArgsConstructor
public enum LoginResultEnum implements IEnum<Integer> {

    // ======

    SUCCESS(0, "登录成功"),
    FAILURE(1, "登录失败"),
    UNKNOWN_FAILURE(2, "未知失败");

    private final Integer value;
    private final String title;

}
