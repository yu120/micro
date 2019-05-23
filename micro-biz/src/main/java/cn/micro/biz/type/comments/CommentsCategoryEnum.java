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

    // ======
    COMMENTS(1, "评论"),
    REPLY(2, "回复");

    private final int value;
    private final String title;

}
