package cn.micro.biz.entity.member;

import cn.micro.biz.commons.mybatis.MicroEntity;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * Role Permission Entity
 *
 * @author lry
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@TableName("role_permission")
public class RolePermissionEntity extends MicroEntity<RolePermissionEntity> {

    private static final long serialVersionUID = 1L;

    /**
     * Role id
     *
     * @see RoleEntity#id
     */
    private Long roleId;
    /**
     * Permission id
     *
     * @see PermissionEntity#id
     */
    private Long permissionId;

}
