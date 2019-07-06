package cn.micro.biz.mapper.member;

import cn.micro.biz.commons.mybatis.extension.IMicroMapper;
import cn.micro.biz.entity.member.RolePermission;
import cn.micro.biz.model.view.RoleCodePermission;

import java.util.List;

/**
 * Role Permission Mapper
 *
 * @author lry
 */
public interface IRolePermissionMapper extends IMicroMapper<RolePermission> {

    /**
     * The select roleCode permission list
     *
     * @return {@link List<RoleCodePermission>}
     */
    List<RoleCodePermission> selectRoleCodePermissions();

    /**
     * The select permission list by roleCode
     *
     * @param roleCode {@link cn.micro.biz.entity.member.Role#code}
     * @return {@link List<RoleCodePermission>}
     */
    List<RoleCodePermission> selectPermissionsByRoleCode(String roleCode);

}