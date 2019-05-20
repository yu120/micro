package cn.micro.biz.service.member.impl;

import cn.micro.biz.commons.mybatis.extension.MicroServiceImpl;
import cn.micro.biz.entity.member.Role;
import cn.micro.biz.mapper.member.IRoleMapper;
import cn.micro.biz.service.member.IRoleService;
import org.springframework.stereotype.Service;

/**
 * Role Service Implements
 *
 * @author lry
 */
@Service
public class RoleServiceImpl extends MicroServiceImpl<IRoleMapper, Role> implements IRoleService {

}
