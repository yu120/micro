package cn.micro.biz.entity.goods;

import cn.micro.biz.commons.mybatis.entity.MicroEntity;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.*;

/**
 * 商品-类别
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
     * 类别名称
     **/
    private String title;
    /**
     * 类别代码
     **/
    private String code;
    /**
     * 类别关键词(多个关键词使用英文逗号分隔)
     **/
    private String keywords;
    /**
     * 类别描述
     **/
    private String intro;
    /**
     * 封面图片url
     **/
    private String imageUrl;
    /**
     * 排列次序
     **/
    private Integer sort;
    /**
     * 状态
     **/
    private Integer status;

    /**
     * 父1级编号（直接父级）
     * {@link GoodsCategory#id}
     **/
    private Long parent1Id;
    /**
     * 父2级编号
     * {@link GoodsCategory#id}
     **/
    private Long parent2Id;
    /**
     * 父3级编号
     * {@link GoodsCategory#id}
     **/
    private Long parent3Id;

}
