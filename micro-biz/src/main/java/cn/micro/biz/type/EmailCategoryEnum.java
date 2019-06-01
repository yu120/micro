package cn.micro.biz.type;

import cn.micro.biz.commons.exception.MicroBadRequestException;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Email Category Enum
 *
 * @author lry
 */
@Getter
@AllArgsConstructor
public enum EmailCategoryEnum {

    // ======

    FORGET_PASSWORD(0, "EMAIL_CODE", "重置密码验证码", "重置密码服务", "重置密码");

    private final int value;
    private final String template;
    private final String subject;
    private final String title;
    private final String category;

    public static EmailCategoryEnum get(Integer value) {
        if (value == null) {
            throw new MicroBadRequestException("验证码类别不能为空");
        }

        for (EmailCategoryEnum e : values()) {
            if (e.getValue() == value) {
                return e;
            }
        }

        throw new MicroBadRequestException("非法验证码类别");
    }

}
