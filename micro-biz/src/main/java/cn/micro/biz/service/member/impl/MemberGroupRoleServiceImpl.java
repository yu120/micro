package cn.micro.biz.service.member.impl;

import cn.micro.biz.commons.mybatis.extension.MicroServiceImpl;
import cn.micro.biz.entity.member.MemberGroupRole;
import cn.micro.biz.mapper.member.IMemberGroupRoleMapper;
import cn.micro.biz.service.member.IMemberGroupRoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Member Group Role Service Implements
 *
 * @author lry
 */
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class MemberGroupRoleServiceImpl extends MicroServiceImpl<IMemberGroupRoleMapper, MemberGroupRole> implements IMemberGroupRoleService {

}
