package cn.micro.biz.type.advertisement;

import cn.micro.biz.commons.enums.IEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

/**
 * Counting Category
 *
 * @author lry
 */
@Getter
@ToString
@AllArgsConstructor
public enum CountingCategoryEnum implements IEnum<Integer> {

    // ====== 商品

    GOODS_COMMENT(101, "商品", "评论数"),
    GOODS_VISIT(102, "商品", "浏览量"),
    GOODS_EXPOSURE(103, "商品", "曝光量"),
    GOODS_COLLECT(104, "商品", "收藏数"),
    GOODS_SALE(105, "商品", "销售量"),

    // ====== 评论

    COMMENT_SUPPORT(201, "评论/回复", "顶数量"),
    COMMENT_OPPOSE(202, "评论/回复", "踩数量"),

    // ====== 动态

    NEWS_PRAISE(301, "动态", "点赞数"),
    NEWS_TRANSMIT(302, "动态", "转发量");

    private final Integer value;
    private final String title;
    private final String msg;

}
