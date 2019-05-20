package cn.micro.biz.entity.goods;

import cn.micro.biz.commons.mybatis.entity.MicroEntity;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.*;

/**
 * 商品
 *
 * @author lry
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@TableName("goods")
public class Goods extends MicroEntity<Goods> {

    private static final long serialVersionUID = 1L;

    /**
     * 商品类别ID
     * {@link GoodsCategory#id}
     */
    private Long categoryId;
    /**
     * 商品品牌ID
     * {@link GoodsBrand#id}
     */
    private Long brandId;
    /**
     * 商品默认规格ID
     * {@link GoodsSpecification#id}
     */
    private Long specificationId;

    /**
     * 商品标题(用于商品列表显示)
     */
    private String title;
    /**
     * 商品LOGO图片url
     **/
    private String logoUrl;

    /**
     * 商品名称(用于描述商品真实名称)
     **/
    private String name;
    /**
     * 商品代码
     */
    private String code;
    /**
     * 状态(下架/上架/预售)
     **/
    private Integer status;
    /**
     * 商品备注
     **/
    private String remark;

}
