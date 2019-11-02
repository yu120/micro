package cn.micro.biz.type.member;

import cn.micro.biz.commons.enums.IEnum;
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
public enum AccountEnum implements IEnum<Integer> {

    // ======

    MOBILE(0, "手机"),
    EMAIL(1, "邮箱"),
    WE_CHAT(2, "微信");

    private final Integer value;
    private final String title;

}
