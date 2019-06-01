package cn.micro.biz.type;

import cn.micro.biz.commons.exception.MicroBadRequestException;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Union Code Category Enum
 *
 * @author lry
 */
@Getter
@AllArgsConstructor
public enum UnionCodeCategoryEnum {

    // ======

    FORGET_PASSWORD(0, "忘记密码验证码", 30 * 60);

    private final int value;
    private final String title;
    /**
     * 单位：秒
     */
    private final int expire;

    public static UnionCodeCategoryEnum get(Integer value) {
        if (value == null) {
            throw new MicroBadRequestException("代码类别不能为空");
        }

        for (UnionCodeCategoryEnum e : values()) {
            if (e.getValue() == value) {
                return e;
            }
        }

        throw new MicroBadRequestException("非法代码类别");
    }

}
