package cn.micro.biz.type.unified;

import cn.micro.biz.commons.enums.IEnum;
import cn.micro.biz.commons.exception.support.MicroBadRequestException;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

/**
 * Union Code Category Enum
 *
 * @author lry
 */
@Getter
@ToString
@AllArgsConstructor
public enum UnionCodeCategoryEnum implements IEnum<Integer> {

    // ======

    FORGET_PASSWORD(0, EmailCategoryEnum.FORGET_PASSWORD.name(), 3, 30 * 60, "忘记密码验证码"),
    CHANGE_EMAIL(1, EmailCategoryEnum.FORGET_PASSWORD.name(), 3, 30 * 60, "修改邮箱验证码"),
    CHANGE_MOBILE(2, EmailCategoryEnum.FORGET_PASSWORD.name(), 3, 30 * 60, "修改手机号验证码");

    private final Integer value;
    private final String category;
    /**
     * 最大验证次数
     */
    private final int maxTimes;
    /**
     * 单位：秒
     */
    private final int expire;
    private final String title;

    public static UnionCodeCategoryEnum get(Integer value) {
        if (value == null) {
            throw new MicroBadRequestException("代码类别不能为空");
        }

        for (UnionCodeCategoryEnum e : values()) {
            if (e.getValue().equals(value)) {
                return e;
            }
        }

        throw new MicroBadRequestException("非法代码类别");
    }

}
