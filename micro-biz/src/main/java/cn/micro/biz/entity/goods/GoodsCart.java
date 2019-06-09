package cn.micro.biz.entity.goods;

import cn.micro.biz.commons.mybatis.MicroEntity;
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
public class GoodsCart extends MicroEntity<GoodsCart> {

    private static final long serialVersionUID = 1L;

    /**
     * Member id
     * <p>
     * {@link cn.micro.biz.entity.member.Member#id}
     **/
    private Long memberId;
    /**
     * Goods id
     * <p>
     * {@link Goods#id}
     **/
    private Long goodsId;
    /**
     * Goods specification id
     * <p>
     * {@link GoodsSpecification#id}
     **/
    private Long goodsSpecificationId;

    /**
     * Goods quantity
     */
    private Integer quantity;

}
