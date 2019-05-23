package cn.micro.biz.entity.score;

import cn.micro.biz.commons.mybatis.entity.MicroEntity;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.*;

import java.math.BigDecimal;

/**
 * Score Details Entity
 * <p>
 * https://blog.csdn.net/lzy_lizhiyang/article/details/77989582
 *
 * @author lry
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@TableName("score_details")
public class ScoreDetails extends MicroEntity<ScoreDetails> {

    private static final long serialVersionUID = 1L;

    /**
     * Score member id
     * <p>
     * {@link cn.micro.biz.entity.member.Member#id}
     */
    private Long memberId;
    /**
     * Score amount
     */
    private BigDecimal operationAmount;
    /**
     * Score available amount
     */
    private BigDecimal availableAmount;
    /**
     * 操作类型：增加积分、减少积分
     */
    private Integer type;
    /**
     * 积分类型：业务类型：消费送、支付扣减、过期等
     */
    private Integer category;
    /**
     * 积分状态：有效、已扣除、已过期、冻结中、冻结返还、冻结扣减
     */
    private Integer status;

    /**
     * 积分来源:当扣减积分时，存储扣减的积分记录id，用于追溯积分
     * <p>
     * {@link ScoreDetails#id}
     */
    private Long origin;

}
