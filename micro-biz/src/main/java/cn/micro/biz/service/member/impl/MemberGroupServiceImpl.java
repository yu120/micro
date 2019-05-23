package cn.micro.biz.service.member.impl;

import cn.micro.biz.commons.mybatis.extension.MicroServiceImpl;
import cn.micro.biz.entity.member.MemberGroup;
import cn.micro.biz.mapper.member.IMemberGroupMapper;
import cn.micro.biz.pubsrv.cache.CacheKey;
import cn.micro.biz.pubsrv.cache.MicroCache;
import cn.micro.biz.service.member.IMemberGroupService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Member Group Service Implements
 *
 * @author lry
 */
@Service
public class MemberGroupServiceImpl extends MicroServiceImpl<IMemberGroupMapper, MemberGroup> implements IMemberGroupService {

    @Override
    @MicroCache(CacheKey.MEMBER_GROUP)
    public List<MemberGroup> memberGroups() {
        return super.list();
    }

}
