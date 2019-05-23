package cn.micro.biz.service.member;

import cn.micro.biz.commons.mybatis.extension.IMicroService;
import cn.micro.biz.entity.member.RolePermission;

import java.util.List;

/**
 * Role Permission Service
 *
 * @author lry
 */
public interface IRolePermissionService extends IMicroService<RolePermission> {

    List<RolePermission> rolePermissions();

}
