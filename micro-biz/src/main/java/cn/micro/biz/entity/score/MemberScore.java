package cn.micro.biz.entity.score;

import cn.micro.biz.commons.mybatis.entity.MicroEntity;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.*;

import java.math.BigDecimal;

/**
 * Member Score Entity
 *
 * @author lry
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@TableName("member_score")
public class MemberScore extends MicroEntity<MemberScore> {

    private static final long serialVersionUID = 1L;

    /**
     * Score member id
     * <p>
     * {@link cn.micro.biz.entity.member.Member#id}
     */
    private Long memberId;
    /**
     * Score all amount
     */
    private BigDecimal allAmount;
    /**
     * Score available amount
     */
    private BigDecimal availableAmount;

}
