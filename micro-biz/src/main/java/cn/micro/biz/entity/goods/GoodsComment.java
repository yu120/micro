package cn.micro.biz.entity.goods;

import cn.micro.biz.commons.mybatis.entity.MicroEntity;
import cn.micro.biz.entity.member.Member;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.*;

import java.util.Date;

/**
 * 商品-评论
 *
 * @author lry
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@TableName("goods_comment")
public class GoodsComment extends MicroEntity<GoodsComment> {

    private static final long serialVersionUID = 1L;

    /**
     * 商品ID
     * {@link Goods#id}
     **/
    private Long goodsId;
    /**
     * 用户ID（评论人）
     * {@link Member#id}
     **/
    private Long memberId;
    /**
     * 父级评论ID
     * {@link GoodsComment#id}
     **/
    private Long parentId;

    /**
     * 评论内容
     **/
    private String content;
    /**
     * 状态(待审核/显示/隐藏)
     **/
    private Integer status;

    /**
     * 审核员ID
     * {@link Member#id}
     **/
    private Long auditorId;
    /**
     * 审核时间
     **/
    private Date auditorTime;
    /**
     * 审核备注
     **/
    private String remark;

    /**
     * 支持数量
     **/
    private Integer supportCount;
    /**
     * 反对数量
     **/
    private Integer opposeCount;

}
