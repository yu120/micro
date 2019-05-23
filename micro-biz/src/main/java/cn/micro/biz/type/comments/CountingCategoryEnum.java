package cn.micro.biz.type.comments;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Counting Category
 *
 * @author lry
 */
@Getter
@AllArgsConstructor
public enum CountingCategoryEnum {

    // ======

    COMMENT(1, "评论数"),
    VISIT(2, "浏览量"),
    EXPOSURE(3, "曝光量"),
    PRAISE(4, "点赞数"),
    TRANSMIT(5, "转发量"),
    COLLECT(6, "收藏数"),
    SALE(7, "销售量");

    private final int value;
    private final String title;

}