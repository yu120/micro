package cn.micro.biz.entity.like;

import cn.micro.biz.commons.mybatis.MicroEntity;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.*;

/**
 * Micro Target Count Entity
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
public class MicroTargetCount extends MicroEntity<MicroTargetCount> {

    /**
     * 点赞目标ID
     */
    private Long targetId;
    /**
     * 点赞目标类型
     * <p>
     * {@link cn.micro.biz.type.like.TargetTypeEnum}
     */
    private Integer targetType;
    /**
     * 被点赞次数
     */
    private Integer times;

}
