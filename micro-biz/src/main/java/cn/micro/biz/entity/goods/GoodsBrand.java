package cn.micro.biz.entity.goods;

import cn.micro.biz.commons.mybatis.entity.MicroEntity;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.*;

/**
 * 商品品牌
 *
 * @author lry
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@TableName("goods_brand")
public class GoodsBrand extends MicroEntity<GoodsBrand> {

    private static final long serialVersionUID = 1L;

    /**
     * 品牌名称
     **/
    private String name;
    /**
     * 品牌LOGO图片url
     **/
    private String logoUrl;
    /**
     * 状态
     **/
    private Integer status;

}
