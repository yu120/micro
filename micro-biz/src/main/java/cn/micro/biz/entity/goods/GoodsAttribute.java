package cn.micro.biz.entity.goods;

import cn.micro.biz.commons.mybatis.MicroEntity;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.*;

/**
 * Goods Attribute Entity
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
    private Long specificationId;

    /**
     * Goods attribute category（Banner/details etc.）
     */
    private Integer category;
    /**
     * Goods attribute code
     **/
    private String code;
    /**
     * Attribute status
     **/
    private Integer status;
    /**
     * Attribute sort
     **/
    private Integer sort;

    // === Basic Attributes

    /**
     * Attributes name
     **/
    private String name;
    /**
     * Attributes value(eg：image url etc.)
     **/
    private String value;
    /**
     * Attributes intro
     **/
    private String intro;

    // === Image Attributes

    /**
     * Image size
     **/
    private Double size;
    /**
     * Image width
     **/
    private Double width;
    /**
     * Image height
     **/
    private Double height;

}
