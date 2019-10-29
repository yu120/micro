package cn.micro.biz.service.member.impl;

import cn.micro.biz.commons.mybatis.extension.MicroServiceImpl;
import cn.micro.biz.entity.member.RolePermissionEntity;
import cn.micro.biz.mapper.member.IRolePermissionMapper;
import cn.micro.biz.service.member.IRolePermissionService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Role Permission Service Implements
 *
 * @author lry
 */
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class RolePermissionServiceImpl extends MicroServiceImpl<IRolePermissionMapper, RolePermissionEntity> implements IRolePermissionService {

}
