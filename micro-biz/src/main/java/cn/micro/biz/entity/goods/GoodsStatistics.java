package cn.micro.biz.entity.goods;

import cn.micro.biz.commons.mybatis.entity.MicroEntity;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.*;

import java.math.BigDecimal;

/**
 * 商品-统计
 *
 * @author lry
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@TableName("goods_statistics")
public class GoodsStatistics extends MicroEntity<GoodsStatistics> {

    private static final long serialVersionUID = 1L;

    /**
     * 商品ID
     * {@link Goods#id}
     **/
    private Long goodsId;

    /**
     * 浏览次数
     **/
    private Integer visitCount;
    /**
     * 评论次数
     **/
    private Integer replyCount;

    /**
     * 销售总量
     **/
    private Integer saleQuantity;
    /**
     * 销售总额
     **/
    private BigDecimal saleAmount;

    /**
     * 进货总量
     **/
    private Integer purchaseQuantity;
    /**
     * 进货总额
     **/
    private BigDecimal purchaseAmount;

    /**
     * 成本均价
     **/
    private BigDecimal costPrice;
    /**
     * 毛利润金额
     **/
    private BigDecimal grossProfit;

}
