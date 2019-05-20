package cn.micro.biz.entity.goods;

import cn.micro.biz.commons.mybatis.entity.MicroEntity;
import cn.micro.biz.entity.member.Member;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.*;

/**
 * 商品-评论-用户
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
     * 商品ID
     * {@link Goods#id}
     **/
    private Long goodsId;
    /**
     * 商品评论ID
     * {@link GoodsComment#id}
     **/
    private Long commentId;
    /**
     * 操作用户ID
     * {@link Member#id}
     **/
    private Long memberId;
    /**
     * 动作类型(支持/反对)
     **/
    private Integer category;

}
