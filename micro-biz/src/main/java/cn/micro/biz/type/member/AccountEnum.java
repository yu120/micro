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
public enum AccountEnum {

    // ======

    MOBILE(0, "手机"),
    EMAIL(1, "邮箱"),
    WX_AUTO_LOGIN(2, "微信自动登录");

    private final int value;
    private final String title;

}
