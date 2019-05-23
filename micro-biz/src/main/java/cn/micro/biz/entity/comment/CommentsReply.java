package cn.micro.biz.entity.comment;

import cn.micro.biz.commons.mybatis.entity.MicroEntity;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.*;

/**
 * Comments Reply Entity
 *
 * @author lry
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@TableName("comments_reply")
public class CommentsReply extends MicroEntity<CommentsReply> {

    private static final long serialVersionUID = 1L;

    /**
     * Comments id
     * <p>
     * {@link Comments#id}
     **/
    private Long commentId;
    /**
     * Reply member id
     * <p>
     * {@link cn.micro.biz.entity.member.Member#id}
     **/
    private Long memberId;
    /**
     * Reply content
     **/
    private String content;

}
