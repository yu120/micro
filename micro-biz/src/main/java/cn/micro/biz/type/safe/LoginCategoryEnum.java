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
public enum LoginCategoryEnum {

    // ======

    SUCCESS(1, "登录成功"),
    FAILURE(2, "登录失败");

    private final int value;
    private final String title;

}
