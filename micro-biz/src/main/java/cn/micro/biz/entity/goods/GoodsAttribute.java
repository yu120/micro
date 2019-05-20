package cn.micro.biz.entity.goods;

import cn.micro.biz.commons.mybatis.entity.MicroEntity;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.*;

/**
 * 商品-属性
 *
 * @author lry
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@TableName("goods_attribute")
public class GoodsAttribute extends MicroEntity<GoodsAttribute> {

    private static final long serialVersionUID = 1L;

    /**
     * 商品ID
     * {@link Goods#id}
     **/
    private Long goodsId;
    /**
     * 商品规格ID
     * {@link GoodsSpecification#id}
     **/
    private Long specificationId;

    /**
     * 图片类别（联动Banner/详情属性）
     */
    private Integer category;
    /**
     * 属性代码
     **/
    private String code;
    /**
     * 状态
     **/
    private Integer status;
    /**
     * 排列次序
     **/
    private Integer sort;

    // === 基本属性

    /**
     * 属性名称
     **/
    private String name;
    /**
     * 属性值(如：图片url等)
     **/
    private String value;
    /**
     * 属性介绍
     **/
    private String intro;

    // === 图片类型属性

    /**
     * 图片大小
     **/
    private Double size;
    /**
     * 图片宽度
     **/
    private Double width;
    /**
     * 图片高度
     **/
    private Double height;

}
