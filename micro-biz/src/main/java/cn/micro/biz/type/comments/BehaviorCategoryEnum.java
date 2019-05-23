package cn.micro.biz.type.comments;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Behavior Category
 *
 * @author lry
 */
@Getter
@AllArgsConstructor
public enum BehaviorCategoryEnum {

    // ======
    SUPPORT(1, "点赞"),
    OPPOSE(2, "踩"),
    TRANSMIT(3, "转发"),
    COLLECT(4, "收藏"),
    ;

    private final int value;
    private final String title;

}
