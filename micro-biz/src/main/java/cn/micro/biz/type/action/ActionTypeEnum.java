package cn.micro.biz.type.action;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

/**
 * 点赞状态
 *
 * @author lry
 */
@Getter
@ToString
@AllArgsConstructor
public enum ActionTypeEnum {

    // ===

    CANCEL(0, "取消"),
    LIKE(1, "点赞");

    private final int value;
    private final String title;

}
