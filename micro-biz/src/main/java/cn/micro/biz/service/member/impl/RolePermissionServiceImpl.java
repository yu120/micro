package cn.micro.biz.service.member.impl;

import cn.micro.biz.commons.mybatis.extension.MicroServiceImpl;
import cn.micro.biz.entity.member.RolePermission;
import cn.micro.biz.mapper.member.IRolePermissionMapper;
import cn.micro.biz.service.member.IRolePermissionService;
import org.springframework.stereotype.Service;

/**
 * 角色-权限 服务实现类
 *
 * @author lry
 */
@Service
public class RolePermissionServiceImpl extends MicroServiceImpl<IRolePermissionMapper, RolePermission> implements IRolePermissionService {

}
