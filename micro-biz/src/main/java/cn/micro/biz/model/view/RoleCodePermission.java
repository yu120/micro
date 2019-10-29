package cn.micro.biz.model.view;

import cn.micro.biz.entity.member.PermissionEntity;
import cn.micro.biz.entity.member.RoleEntity;
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
public class RoleCodePermission extends PermissionEntity {

    /**
     * Member id
     * <p>
     * {@link RoleEntity#code}
     */
    private String roleCode;

}
