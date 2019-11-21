package cn.micro.biz.service.member.impl;

import cn.micro.biz.entity.member.MemberGroupRoleEntity;
import cn.micro.biz.mapper.member.IMemberGroupRoleMapper;
import cn.micro.biz.service.member.IMemberGroupRoleService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
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
public class MemberGroupRoleServiceImpl extends ServiceImpl<IMemberGroupRoleMapper, MemberGroupRoleEntity> implements IMemberGroupRoleService {

}
