package cn.micro.biz.type.member;

import cn.micro.biz.commons.mybatis.extension.IEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

/**
 * Account Category
 *
 * @author lry
 */
@Getter
@ToString
@AllArgsConstructor
public enum AccountEnum implements IEnum<AccountEnum, Integer> {

    // ======

    MOBILE(0, "手机"),
    EMAIL(23, "邮箱"),
    WX_AUTO_LOGIN(2, "微信自动登录");

    private final Integer value;
    private final String title;

}
