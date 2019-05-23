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
public enum BehaviorOriginEnum {

    // ======
    GOODS(1, "商品行为"),
    COMMENT(2, "评论行为:顶、踩"),
    ;

    private final int value;
    private final String title;

}
