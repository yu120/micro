package cn.micro.biz.type.advertisement;

import cn.micro.biz.commons.enums.IEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

/**
 * Comments Category
 *
 * @author lry
 */
@Getter
@ToString
@AllArgsConstructor
public enum CommentsCategoryEnum implements IEnum<Integer> {

    // ====== 商品

    GOODS_COMMENTS(101, "商品", "评论/回复"),

    // ====== 动态

    NEWS_COMMENTS(201, "动态", "评论/回复");

    private final Integer value;
    private final String title;
    private final String msg;

}
