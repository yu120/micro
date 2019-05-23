package cn.micro.biz.type.member;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Account Category
 *
 * @author lry
 */
@Getter
@AllArgsConstructor
public enum AccountCategoryEnum {

    // ======

    EMAIL(1, "邮箱"),
    MOBILE(2, "手机");

    private final int value;
    private final String title;

}
