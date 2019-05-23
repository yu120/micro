package cn.micro.biz.entity.goods;

import cn.micro.biz.commons.mybatis.entity.MicroEntity;
import cn.micro.biz.entity.member.Member;
import cn.micro.biz.type.goods.GoodsCountCategoryEnum;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.*;

/**
 * Goods Record Entity
 *
 * @author lry
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@TableName("goods_record")
public class GoodsRecord extends MicroEntity<GoodsRecord> {

    private static final long serialVersionUID = 1L;

    /**
     * Goods id
     * <p>
     * {@link Goods#id}
     **/
    private Long goodsId;
    /**
     * Member id
     * <p>
     * {@link Member#id}
     **/
    private Long memberId;
    /**
     * Record category
     * <p>
     * {@link cn.micro.biz.type.goods.GoodsRecordCategoryEnum}
     */
    private Integer category;

}
