package cn.micro.biz.type.comments;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Comments Category
 *
 * @author lry
 */
@Getter
@AllArgsConstructor
public enum CommentsCategoryEnum {

    // ====== 商品

    GOODS_COMMENTS(101, "商品", "评论/回复"),

    // ====== 动态

    NEWS_COMMENTS(201, "动态", "评论/回复"),
    ;

    private final int value;
    private final String title;
    private final String msg;

}
