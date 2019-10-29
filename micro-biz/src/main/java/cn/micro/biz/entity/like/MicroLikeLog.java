package cn.micro.biz.entity.like;

import cn.micro.biz.commons.mybatis.MicroEntity;
import cn.micro.biz.model.like.OwnerAction;
import cn.micro.biz.model.like.TargetAction;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.*;

/**
 * Micro Like Log Entity
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
@TableName("micro_like_log")
public class MicroLikeLog extends MicroEntity<MicroLikeLog> {

    /**
     * 点赞状态
     * <p>
     * {@link cn.micro.biz.type.like.ActionTypeEnum}
     */
    private Integer status;
    /**
     * 点赞发起者ID
     */
    private Long ownerId;
    /**
     * 点赞发起者类型
     * <p>
     * {@link cn.micro.biz.type.like.OwnerTypeEnum}
     */
    private Integer ownerType;
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

    public MicroLikeLog(OwnerAction ownerAction, TargetAction targetAction) {
        this.ownerId = ownerAction.getOwnerId();
        this.ownerType = ownerAction.getOwnerType();
        this.targetId = targetAction.getTargetId();
        this.targetType = targetAction.getTargetType();
    }
    
}
