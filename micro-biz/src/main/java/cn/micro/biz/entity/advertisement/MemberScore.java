package cn.micro.biz.entity.advertisement;

import cn.micro.biz.commons.mybatis.MicroEntity;
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
     *
     * @see cn.micro.biz.entity.member.Member#id
     */
    private Long memberId;
    /**
     * Score all amount
     */
    private BigDecimal amount;

}
