package cn.micro.biz.entity.advertisement;

import cn.micro.biz.commons.mybatis.MicroEntity;
import cn.micro.biz.type.advertisement.CommentsCategoryEnum;
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
     * Parent comments id
     *
     * @see Comments#id
     **/
    private Long parentId;
    /**
     * Comments member id
     *
     * @see cn.micro.biz.entity.member.Member#id
     **/
    private Long memberId;
    /**
     * Comments origin id
     */
    private Long originId;

    /**
     * Comments category
     *
     * @serial tinyint(3)
     */
    private CommentsCategoryEnum category;
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
     *
     * @see cn.micro.biz.entity.member.Member#id
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
