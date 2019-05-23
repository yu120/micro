package cn.micro.biz.type.comments;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Origin Category
 *
 * @author lry
 */
@Getter
@AllArgsConstructor
public enum CommentsOriginEnum {

    // ======
    GOODS(1, "商品行为"),
    ;

    private final int value;
    private final String title;

}
