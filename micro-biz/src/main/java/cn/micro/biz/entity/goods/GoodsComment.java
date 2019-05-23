package cn.micro.biz.entity.goods;

import cn.micro.biz.commons.mybatis.entity.MicroEntity;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.*;

import java.util.Date;

/**
 * Goods Comment Entity
 *
 * @author lry
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@TableName("goods_comment")
public class GoodsComment extends MicroEntity<GoodsComment> {

    private static final long serialVersionUID = 1L;

    /**
     * Goods id
     * <p>
     * {@link Goods#id}
     **/
    private Long goodsId;
    /**
     * Comment member id
     * <p>
     * {@link cn.micro.biz.entity.member.Member#id}
     **/
    private Long memberId;
    /**
     * Parent comment id
     * <p>
     * {@link GoodsComment#id}
     **/
    private Long parentId;

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
     * Support count number
     **/
    private Integer support;
    /**
     * Oppose count number
     **/
    private Integer oppose;

}
