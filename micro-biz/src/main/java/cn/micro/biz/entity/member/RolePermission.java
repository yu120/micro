package cn.micro.biz.entity.member;

import cn.micro.biz.commons.mybatis.entity.MicroEntity;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * 角色权限
 *
 * @author lry
 * @since 2018-10-19
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@TableName("role_permission")
public class RolePermission extends MicroEntity<RolePermission> {

    private static final long serialVersionUID = 1L;

    /**
     * {@link Role#id}
     */
    private Long roleId;
    /**
     * {@link Permission#id}
     */
    private Long permissionId;

}
