package cn.micro.biz.service.member.impl;

import cn.micro.biz.commons.mybatis.extension.MicroServiceImpl;
import cn.micro.biz.entity.member.Permission;
import cn.micro.biz.mapper.member.IPermissionMapper;
import cn.micro.biz.pubsrv.cache.CacheKey;
import cn.micro.biz.pubsrv.cache.MicroCache;
import cn.micro.biz.service.member.*;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Permission Service Implements
 *
 * @author lry
 */
@Service
public class PermissionServiceImpl extends MicroServiceImpl<IPermissionMapper, Permission> implements IPermissionService {

    @Override
    @MicroCache(CacheKey.PERMISSION)
    public List<Permission> permissions() {
        return super.list();
    }

}
