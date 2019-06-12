package cn.micro.biz.entity.goods;

import cn.micro.biz.commons.mybatis.MicroEntity;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.*;

/**
 * Goods Category Entity
 *
 * @author lry
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@TableName("goods_category")
public class GoodsCategory extends MicroEntity<GoodsCategory> {

    private static final long serialVersionUID = 1L;

    /**
     * Goods category title
     **/
    private String title;
    /**
     * Goods category code
     **/
    private String code;
    /**
     * Goods category keywords list(Multiple keywords are separated by ',' in English)
     **/
    private String keywords;
    /**
     * Goods category intro
     **/
    private String intro;
    /**
     * Goods category image url
     **/
    private String imageUrl;
    /**
     * Goods category sort
     **/
    private Integer sort;
    /**
     * Goods category status
     **/
    private Integer status;

    /**
     * Level 1 parent goods category id(Direct parent)
     *
     * @see GoodsCategory#id
     **/
    private Long parent1Id;
    /**
     * Level 2 parent goods category id
     *
     * @see GoodsCategory#id
     **/
    private Long parent2Id;
    /**
     * Level 3 parent goods category id
     *
     * @see GoodsCategory#id
     **/
    private Long parent3Id;

}
