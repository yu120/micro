package cn.micro.biz.service.member.impl;

import cn.micro.biz.commons.mybatis.extension.MicroServiceImpl;
import cn.micro.biz.entity.member.MemberGroupRole;
import cn.micro.biz.mapper.member.IMemberGroupRoleMapper;
import cn.micro.biz.pubsrv.cache.CacheKey;
import cn.micro.biz.pubsrv.cache.MicroCache;
import cn.micro.biz.service.member.IMemberGroupRoleService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Member Group Role Service Implements
 *
 * @author lry
 */
@Service
public class MemberGroupRoleServiceImpl extends MicroServiceImpl<IMemberGroupRoleMapper, MemberGroupRole> implements IMemberGroupRoleService {

    @Override
    @MicroCache(CacheKey.ALL_MEMBER_GROUP_ROLE)
    public List<MemberGroupRole> memberGroupRoles() {
        return super.list();
    }

}
