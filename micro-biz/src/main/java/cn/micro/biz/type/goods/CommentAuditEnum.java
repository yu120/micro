package cn.micro.biz.type.goods;

import cn.micro.biz.commons.enums.IEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

/**
 * Comment Audit Category
 *
 * @author lry
 */
@Getter
@ToString
@AllArgsConstructor
public enum CommentAuditEnum  implements IEnum<Integer> {

    // ======

    AUTOMATIC(0, "自动审核"),
    ARTIFICIAL(1, "人工审核");

    private final Integer value;
    private final String title;

}
