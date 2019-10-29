package cn.micro.biz.entity.praise;

import cn.micro.biz.commons.mybatis.MicroEntity;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.*;

/**
 * Praise Log Entity
 * <p>
 * 主要用于记录用户操作日志，不参与业务逻辑。
 *
 * @author lry
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@TableName("praise_log")
public class PraiseLog extends MicroEntity<PraiseLog> {

    /**
     * {@link cn.micro.biz.pubsrv.ActionEnum}
     */
    private Integer actionType;
    private Long ownerId;
    private Integer ownerType;
    private Long targetId;
    private Integer targetType;

}
