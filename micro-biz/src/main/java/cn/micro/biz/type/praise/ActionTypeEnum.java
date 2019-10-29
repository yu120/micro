package cn.micro.biz.type.praise;

import cn.micro.biz.commons.enums.IEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

/**
 * Praise Action Type
 *
 * @author lry
 */
@Getter
@ToString
@AllArgsConstructor
public enum ActionTypeEnum implements IEnum<Integer> {

    // ===

    CANCEL(0, "取消"),
    LIKE(1, "提交");

    private final int value;
    private final String title;

}
