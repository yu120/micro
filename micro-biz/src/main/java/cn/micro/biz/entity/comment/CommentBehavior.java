package cn.micro.biz.entity.comment;

import cn.micro.biz.commons.mybatis.entity.MicroEntity;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.*;

/**
 * Comment Behavior Entity
 *
 * @author lry
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@TableName("comment_behavior")
public class CommentBehavior extends MicroEntity<CommentBehavior> {

    private static final long serialVersionUID = 1L;

    /**
     * Goods comment id
     * <p>
     * {@link Comment#id}
     **/
    private Long commentId;
    /**
     * Comment member id
     * <p>
     * {@link cn.micro.biz.entity.member.Member#id}
     **/
    private Long memberId;
    /**
     * Comment category(support/oppose)
     **/
    private Integer category;

}
