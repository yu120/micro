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
public class RolePermission extends MicroEntity<RolePermission> {

    private static final long serialVersionUID = 1L;

    /**
     * Role id
     * <p>
     * {@link Role#id}
     */
    private Long roleId;
    /**
     * Permission id
     * <p>
     * {@link Permission#id}
     */
    private Long permissionId;

}
