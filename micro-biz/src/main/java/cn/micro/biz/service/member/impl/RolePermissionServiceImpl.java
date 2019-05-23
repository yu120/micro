package cn.micro.biz.service.member.impl;

import cn.micro.biz.commons.mybatis.extension.MicroServiceImpl;
import cn.micro.biz.entity.member.RolePermission;
import cn.micro.biz.mapper.member.IRolePermissionMapper;
import cn.micro.biz.pubsrv.cache.CacheKey;
import cn.micro.biz.pubsrv.cache.MicroCache;
import cn.micro.biz.service.member.IRolePermissionService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Role Permission Service Implements
 *
 * @author lry
 */
@Service
public class RolePermissionServiceImpl extends MicroServiceImpl<IRolePermissionMapper, RolePermission> implements IRolePermissionService {

    @Override
    @MicroCache(CacheKey.ROLE_PERMISSION)
    public List<RolePermission> rolePermissions() {
        return super.list();
    }

}
