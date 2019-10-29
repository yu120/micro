package cn.micro.biz.entity.action;

import cn.micro.biz.commons.mybatis.MicroEntity;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.*;

/**
 * 目标统计
 * <p>
 *
 * @author lry
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@TableName("micro_target_count")
public class TargetCount extends MicroEntity<TargetCount> {

    /**
     * 目标ID
     */
    private Long targetId;
    /**
     * 目标类型
     * <p>
     * {@link cn.micro.biz.type.action.TargetTypeEnum}
     */
    private Integer targetType;
    /**
     * 目标统计数量
     */
    private Integer countNum;

}
