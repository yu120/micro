package cn.micro.biz.entity.like;

import cn.micro.biz.commons.mybatis.MicroEntity;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.*;

/**
 * Micro Like Log Entity
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
public class MicroLikeLog extends MicroEntity<MicroLikeLog> {

    /**
     * {@link cn.micro.biz.type.like.ActionTypeEnum}
     */
    private Integer actionType;
    private Long ownerId;
    private Integer ownerType;
    private Long targetId;
    private Integer targetType;

}
