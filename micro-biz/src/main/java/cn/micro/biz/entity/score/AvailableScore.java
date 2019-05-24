package cn.micro.biz.entity.score;

import cn.micro.biz.commons.mybatis.entity.MicroEntity;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.*;

import java.math.BigDecimal;
import java.util.Date;

/**
 * Member Available Score Entity
 *
 * @author lry
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@TableName("available_score")
public class AvailableScore extends MicroEntity<AvailableScore> {

    private static final long serialVersionUID = 1L;

    /**
     * Score member id
     * <p>
     * {@link cn.micro.biz.entity.member.Member#id}
     */
    private Long memberId;
    /**
     * Score available amount
     */
    private BigDecimal amount;
    /**
     * Has it expired
     */
    private Integer expire;
    /**
     * Expire time
     */
    private Date expireTime;

}
