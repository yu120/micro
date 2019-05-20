package cn.micro.biz.entity.goods;

import cn.micro.biz.commons.mybatis.entity.MicroEntity;
import cn.micro.biz.entity.member.Member;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.*;

/**
 * Goods Comment Member Entity
 *
 * @author lry
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@TableName("goods_comment_member")
public class GoodsCommentMember extends MicroEntity<GoodsCommentMember> {

    private static final long serialVersionUID = 1L;

    /**
     * Comment goods id
     * <p>
     * {@link Goods#id}
     **/
    private Long goodsId;
    /**
     * Goods comment id
     * <p>
     * {@link GoodsComment#id}
     **/
    private Long commentId;
    /**
     * Comment member id
     * <p>
     * {@link Member#id}
     **/
    private Long memberId;
    /**
     * Comment category(support/oppose)
     **/
    private Integer category;

}
