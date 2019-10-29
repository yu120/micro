package cn.micro.biz.entity.goods;

import cn.micro.biz.commons.mybatis.MicroEntity;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.*;

import java.math.BigDecimal;

/**
 * Goods Statistics Entity
 *
 * @author lry
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@TableName("goods_statistics")
public class GoodsStatisticsEntity extends MicroEntity<GoodsStatisticsEntity> {

    private static final long serialVersionUID = 1L;

    /**
     * Goods id
     *
     * @see GoodsEntity#id
     **/
    private Long goodsId;

    /**
     * Sale quantity
     **/
    private Integer saleQuantity;
    /**
     * Sale amount
     **/
    private BigDecimal saleAmount;

    /**
     * Purchase quantity
     **/
    private Integer purchaseQuantity;
    /**
     * Purchase amount
     **/
    private BigDecimal purchaseAmount;

    /**
     * Cost price
     **/
    private BigDecimal costPrice;
    /**
     * Gross profit
     **/
    private BigDecimal grossProfit;

}
