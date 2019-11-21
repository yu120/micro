package cn.micro.biz.service.member.impl;

import cn.micro.biz.entity.member.RoleEntity;
import cn.micro.biz.mapper.member.IRoleMapper;
import cn.micro.biz.service.member.IRoleService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Role Service Implements
 *
 * @author lry
 */
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class RoleServiceImpl extends ServiceImpl<IRoleMapper, RoleEntity> implements IRoleService {


}
