package cn.micro.biz.model.view;

import cn.micro.biz.entity.member.Permission;
import lombok.*;

/**
 * Role Code Permission
 *
 * @author lry
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class RoleCodePermission extends Permission {

    /**
     * Member id
     * <p>
     * {@link cn.micro.biz.entity.member.Role#code}
     */
    private String roleCode;

}
