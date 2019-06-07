package cn.micro.biz.type.safe;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Login Category
 *
 * @author lry
 */
@Getter
@AllArgsConstructor
public enum LoginResultEnum {

    // ======

    SUCCESS(0, "登录成功"),
    FAILURE(1, "登录失败"),
    UNKNOWN_FAILURE(2, "未知失败");

    private final int value;
    private final String title;

}
