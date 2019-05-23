package cn.micro.biz.type.goods;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Comment Audit Category
 *
 * @author lry
 */
@Getter
@AllArgsConstructor
public enum CommentAuditEnum {

    // ======

    AUTOMATIC(1, "自动审核"),
    ARTIFICIAL(2, "人工审核");

    private final int value;
    private final String title;

}
