package cn.micro.biz.service.member.impl;

import cn.micro.biz.commons.mybatis.extension.MicroServiceImpl;
import cn.micro.biz.entity.member.Permission;
import cn.micro.biz.mapper.member.IPermissionMapper;
import cn.micro.biz.service.member.*;
import org.springframework.stereotype.Service;

/**
 * Permission Service Implements
 *
 * @author lry
 */
@Service
public class PermissionServiceImpl extends MicroServiceImpl<IPermissionMapper, Permission> implements IPermissionService {

}
