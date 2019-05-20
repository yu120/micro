package cn.micro.biz.entity.goods;

import cn.micro.biz.commons.mybatis.entity.MicroEntity;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.*;

import java.math.BigDecimal;

/**
 * 商品-规格
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
     * 商品ID
     * {@link Goods#id}
     **/
    private Long goodsId;

    /**
     * 状态
     **/
    private Integer status;
    /**
     * 排序
     */
    private Integer sort;
    /**
     * 规格名称
     **/
    private String name;
    /**
     * 规格提示信息
     */
    private String tip;
    /**
     * 规格图片url
     **/
    private String imageUrl;
    /**
     * 介绍
     */
    private String intro;

    /**
     * 库存量
     **/
    private Integer stock;
    /**
     * 告警库存
     **/
    private Integer warnStock;

    /**
     * 商品价格(销售价格)
     **/
    private BigDecimal price;
    /**
     * 成本价格(进货价格)
     **/
    private BigDecimal costPrice;
    /**
     * 市场价格
     **/
    private BigDecimal marketPrice;

}
