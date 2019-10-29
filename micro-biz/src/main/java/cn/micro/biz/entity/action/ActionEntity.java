package cn.micro.biz.entity.action;

import cn.micro.biz.commons.mybatis.MicroEntity;
import cn.micro.biz.model.like.OwnerAction;
import cn.micro.biz.model.like.TargetAction;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.*;

/**
 * 行为动作
 * <p>
 * Index：owner_type,owner_id,target_type,target_id
 *
 * @author lry
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@TableName("action")
public class ActionEntity extends MicroEntity<ActionEntity> {

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

    public ActionEntity(OwnerAction ownerAction, TargetAction targetAction) {
        this.ownerType = ownerAction.getOwnerType();
        this.ownerId = ownerAction.getOwnerId();
        this.targetType = targetAction.getTargetType();
        this.targetId = targetAction.getTargetId();
    }

}
