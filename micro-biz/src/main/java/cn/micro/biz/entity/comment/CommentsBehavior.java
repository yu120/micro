package cn.micro.biz.entity.comment;

import cn.micro.biz.commons.mybatis.entity.MicroEntity;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.*;

/**
 * Comments Behavior Entity
 *
 * @author lry
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@TableName("comments_behavior")
public class CommentsBehavior extends MicroEntity<CommentsBehavior> {

    private static final long serialVersionUID = 1L;

    /**
     * Comments id
     * <p>
     * {@link Comments#id}
     **/
    private Long commentId;
    /**
     * Comments member id
     * <p>
     * {@link cn.micro.biz.entity.member.Member#id}
     **/
    private Long memberId;
    /**
     * Comments category(support/oppose)
     **/
    private Integer category;

}
