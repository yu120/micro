package cn.micro.biz.entity.goods;

import cn.micro.biz.commons.mybatis.MicroEntity;
import cn.micro.biz.type.goods.CommentAuditEnum;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.*;

/**
 * Goods Entity
 *
 * @author lry
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@TableName("goods")
public class GoodsEntity extends MicroEntity<GoodsEntity> {

    private static final long serialVersionUID = 1L;

    /**
     * Goods category id
     *
     * @see GoodsCategoryEntity#id
     */
    private Long categoryId;
    /**
     * Goods brand id
     *
     * @see GoodsBrandEntity#id
     */
    private Long brandId;
    /**
     * Goods default specification id
     *
     * @see GoodsSpecificationEntity#id
     */
    private Long specificationId;

    /**
     * Goods title(Used for goods list display)
     */
    private String title;
    /**
     * Goods LOGO image link url
     **/
    private String logoUrl;
    /**
     * Goods name(Used to describe the real name of a goods)
     **/
    private String name;
    /**
     * Goods code(Fixed unique identification)
     */
    private String code;
    /**
     * Goods status(Off-shelf,on-shelf,pre-sale etc.)
     **/
    private Integer status;
    /**
     * Comment audit way(automatic/artificial)
     */
    private CommentAuditEnum commentAudit;
    /**
     * Goods remark
     **/
    private String remark;

}
