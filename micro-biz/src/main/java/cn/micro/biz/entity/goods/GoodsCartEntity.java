package cn.micro.biz.entity.goods;

import cn.micro.biz.commons.mybatis.MicroEntity;
import cn.micro.biz.entity.member.MemberEntity;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.*;

/**
 * Goods Cart Entity
 *
 * @author lry
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@TableName("goods_cart")
public class GoodsCartEntity extends MicroEntity<GoodsCartEntity> {

    private static final long serialVersionUID = 1L;

    /**
     * Member id
     *
     * @see MemberEntity#id
     **/
    private Long memberId;
    /**
     * Goods id
     *
     * @see GoodsEntity#id
     **/
    private Long goodsId;
    /**
     * Goods specification id
     *
     * @see GoodsSpecificationEntity#id
     **/
    private Long goodsSpecificationId;

    /**
     * Goods quantity
     */
    private Integer quantity;

}
