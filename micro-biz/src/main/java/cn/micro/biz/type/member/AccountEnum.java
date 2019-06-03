package cn.micro.biz.type.member;

import com.baomidou.mybatisplus.annotation.EnumValue;
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
    EMAIL(23, "邮箱"),
    WX_AUTO_LOGIN(2, "微信自动登录");

    @EnumValue
    private final int value;
    private final String title;

    public static AccountEnum parse(int value) {
        for (AccountEnum e : values()) {
            if (e.getValue() == value) {
                return e;
            }
        }

        throw new IllegalArgumentException("Illegal Argument");
    }

}
