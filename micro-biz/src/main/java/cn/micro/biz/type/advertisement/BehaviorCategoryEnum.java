package cn.micro.biz.type.advertisement;

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

    // ====== 商品

    GOODS_COLLECT(101, "商品", "收藏"),
    GOODS_VISIT(102, "商品", "浏览量"),

    // ====== 评论/回复

    COMMENT_SUPPORT(201, "评论/回复", "顶"),
    COMMENT_OPPOSE(202, "评论/回复", "踩"),

    // ====== 动态

    NEWS_TRANSMIT(301, "动态", "转发"),
    NEWS_COLLECT(302, "动态", "收藏"),
    ;

    private final int value;
    private final String title;
    private final String msg;

}
