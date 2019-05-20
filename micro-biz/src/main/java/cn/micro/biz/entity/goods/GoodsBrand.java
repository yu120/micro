package cn.micro.biz.entity.goods;

import cn.micro.biz.commons.mybatis.entity.MicroEntity;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.*;

/**
 * Goods Brand Entity
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
     * Goods brand name
     **/
    private String name;
    /**
     * Brand LOGO image link url
     **/
    private String logoUrl;
    /**
     * Goods brand status
     **/
    private Integer status;

}
