package cn.micro.biz.entity.goods;

import cn.micro.biz.commons.mybatis.entity.MicroEntity;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.*;

/**
 * Goods Comment Behavior Entity
 *
 * @author lry
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@TableName("goods_comment_behavior")
public class GoodsCommentBehavior extends MicroEntity<GoodsCommentBehavior> {

    private static final long serialVersionUID = 1L;

    /**
     * Goods comment id
     * <p>
     * {@link GoodsComment#id}
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
