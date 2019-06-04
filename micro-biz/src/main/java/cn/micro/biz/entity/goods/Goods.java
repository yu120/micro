package cn.micro.biz.entity.goods;

import cn.micro.biz.commons.mybatis.MicroEntity;
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
public class Goods extends MicroEntity<Goods> {

    private static final long serialVersionUID = 1L;

    /**
     * Goods category id
     * <p>
     * {@link GoodsCategory#id}
     */
    private Long categoryId;
    /**
     * Goods brand id
     * <p>
     * {@link GoodsBrand#id}
     */
    private Long brandId;
    /**
     * Goods default specification id
     * <p>
     * {@link GoodsSpecification#id}
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
     * <p>
     * {@link cn.micro.biz.type.goods.CommentAuditEnum}
     */
    private Integer commentAudit;
    /**
     * Goods remark
     **/
    private String remark;

}
