package cn.micro.biz.service.member.impl;

import cn.micro.biz.entity.member.MemberRole;
import cn.micro.biz.mapper.member.IMemberRoleMapper;
import cn.micro.biz.commons.mybatis.extension.MicroServiceImpl;
import cn.micro.biz.service.member.IMemberRoleService;
import org.springframework.stereotype.Service;

/**
 * 用户-角色 服务实现类
 *
 * @author lry
 */
@Service
public class MemberRoleServiceImpl extends MicroServiceImpl<IMemberRoleMapper, MemberRole> implements IMemberRoleService {

}
