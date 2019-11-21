package cn.micro.biz.mapper.member;

import cn.micro.biz.entity.member.RoleEntity;
import cn.micro.biz.entity.member.RolePermissionEntity;
import cn.micro.biz.model.view.RoleCodePermission;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;

/**
 * Role Permission Mapper
 *
 * @author lry
 */
public interface IRolePermissionMapper extends BaseMapper<RolePermissionEntity> {

    /**
     * The select roleCode permission list
     *
     * @return {@link List<RoleCodePermission>}
     */
    List<RoleCodePermission> selectRoleCodePermissions();

    /**
     * The select permission list by roleCode
     *
     * @param roleCode {@link RoleEntity#code}
     * @return {@link List<RoleCodePermission>}
     */
    List<RoleCodePermission> selectPermissionsByRoleCode(String roleCode);

}