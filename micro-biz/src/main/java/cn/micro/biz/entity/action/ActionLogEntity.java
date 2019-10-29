package cn.micro.biz.entity.action;

import cn.micro.biz.commons.mybatis.MicroEntity;
import cn.micro.biz.model.like.OwnerAction;
import cn.micro.biz.model.like.TargetAction;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.*;

/**
 * 行为动作日志
 * <p>
 * 主要用于记录用户操作日志，不参与业务逻辑, 可用于分析用户行为。
 *
 * @author lry
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@TableName("action_log")
public class ActionLogEntity extends MicroEntity<ActionLogEntity> {

    /**
     * 操作状态
     * <p>
     * {@link cn.micro.biz.type.action.ActionTypeEnum}
     */
    private Integer status;
    /**
     * 发起者类型
     * <p>
     * {@link cn.micro.biz.type.action.OwnerTypeEnum}
     */
    private Integer ownerType;
    /**
     * 发起者ID
     */
    private Long ownerId;
    /**
     * 目标类型
     * <p>
     * {@link cn.micro.biz.type.action.TargetTypeEnum}
     */
    private Integer targetType;
    /**
     * 目标ID
     */
    private Long targetId;

    public ActionLogEntity(OwnerAction ownerAction, TargetAction targetAction) {
        this.ownerId = ownerAction.getOwnerId();
        this.ownerType = ownerAction.getOwnerType();
        this.targetId = targetAction.getTargetId();
        this.targetType = targetAction.getTargetType();
    }

}
