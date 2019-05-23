package cn.micro.biz.entity.comment;

import cn.micro.biz.commons.mybatis.entity.MicroEntity;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.*;

import java.util.Date;

/**
 * Comment Entity
 *
 * @author lry
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@TableName("comment")
public class Comment extends MicroEntity<Comment> {

    private static final long serialVersionUID = 1L;

    /**
     * Parent comment id
     * <p>
     * {@link Comment#id}
     **/
    private Long parentId;

    /**
     * Comment category
     */
    private Integer category;
    /**
     * Comment category id
     */
    private Long categoryId;

    /**
     * Comment member id
     * <p>
     * {@link cn.micro.biz.entity.member.Member#id}
     **/
    private Long memberId;

    /**
     * Comment content
     **/
    private String content;
    /**
     * Comment status(Wait-audit/displayed/hidden)
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
