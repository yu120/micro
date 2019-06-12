package cn.micro.biz.entity.goods;

import cn.micro.biz.commons.mybatis.MicroEntity;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.*;

import java.math.BigDecimal;

/**
 * Goods Specification Entity
 *
 * @author lry
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@TableName("goods_specification")
public class GoodsSpecification extends MicroEntity<GoodsSpecification> {

    private static final long serialVersionUID = 1L;

    /**
     * Goods id
     *
     * @see Goods#id
     **/
    private Long goodsId;

    /**
     * Goods specification status
     **/
    private Integer status;
    /**
     * Specification sort
     */
    private Integer sort;
    /**
     * Specification name
     **/
    private String name;
    /**
     * 规格提示信息
     */
    private String tip;
    /**
     * Specification image link url
     **/
    private String imageUrl;
    /**
     * Specification intro
     */
    private String intro;

    /**
     * stock
     **/
    private Integer stock;
    /**
     * Warn stock
     **/
    private Integer warnStock;

    /**
     * Goods price(Selling price)
     **/
    private BigDecimal price;
    /**
     * Cost price(Purchase price)
     **/
    private BigDecimal costPrice;
    /**
     * Market price
     **/
    private BigDecimal marketPrice;

}
