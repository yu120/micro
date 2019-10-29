package cn.micro.biz.entity.praise;

import cn.micro.biz.commons.mybatis.MicroEntity;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.*;

/**
 * Praise Entity
 * <p>
 * Index：owner_type,owner_id,target_type,target_id
 * Praise是该系统的核心，承载核心业务逻辑。
 *
 * @author lry
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@TableName("praise")
public class Praise extends MicroEntity<Praise> {

    /**
     * {@link cn.micro.biz.type.praise.ActionTypeEnum}
     */
    private Integer status;
    private Long ownerId;
    private Integer ownerType;
    private Long targetId;
    private Integer targetType;

}
