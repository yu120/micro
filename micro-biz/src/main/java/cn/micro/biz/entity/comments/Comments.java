package cn.micro.biz.entity.comments;

import cn.micro.biz.commons.mybatis.MicroEntity;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.*;

import java.util.Date;

/**
 * Comments Entity
 *
 * @author lry
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@TableName("comments")
public class Comments extends MicroEntity<Comments> {

    private static final long serialVersionUID = 1L;

    /**
     * Comments category
     * <p>
     * {@link cn.micro.biz.type.comments.CommentsCategoryEnum}
     */
    private Integer category;
    /**
     * Comments origin id
     */
    private Long originId;
    /**
     * Comments member id
     * <p>
     * {@link cn.micro.biz.entity.member.Member#id}
     **/
    private Long memberId;
    /**
     * Parent comments id
     * <p>
     * {@link Comments#id}
     **/
    private Long parentId;

    /**
     * Comments content
     **/
    private String content;
    /**
     * Comments status(Wait-audit/displayed/hidden)
     **/
    private Integer status;

    /**
     * Auditor member id
     * <p>
     * {@link cn.micro.biz.entity.member.Member#id}
     **/
    private Long auditId;
    /**
     * Audit time
     **/
    private Date auditTime;
    /**
     * Audit remark
     **/
    private String auditRemark;

    /**
     * Support quantity
     **/
    private Integer support;
    /**
     * Oppose quantity
     **/
    private Integer oppose;

}
