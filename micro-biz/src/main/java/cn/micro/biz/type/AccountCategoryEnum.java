package cn.micro.biz.type;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 账号类型
 *
 * @author lry
 */
@Getter
@AllArgsConstructor
public enum AccountCategoryEnum {

    // ======

    EMAIL(1, "邮箱"),
    MOBILE(2, "手机"),
    ID_CARD(3, "身份证");

    private final int value;
    private final String title;

}
