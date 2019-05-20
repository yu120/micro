package cn.micro.biz.service.member.impl;

import cn.micro.biz.commons.auth.MicroAuthContext;
import cn.micro.biz.commons.mybatis.extension.MicroServiceImpl;
import cn.micro.biz.entity.member.MemberRole;
import cn.micro.biz.entity.member.Permission;
import cn.micro.biz.mapper.member.IPermissionMapper;
import cn.micro.biz.service.member.*;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * 权限 服务实现类
 *
 * @author lry
 */
@Service
public class PermissionServiceImpl extends MicroServiceImpl<IPermissionMapper, Permission> implements IPermissionService {

    @Resource
    private IMemberGroupRoleService memberGroupRoleService;
    @Resource
    private IMemberGroupMemberService memberGroupMemberService;

    @Resource
    private IMemberRoleService memberRoleService;
    @Resource
    private IRolePermissionService rolePermissionService;

    public void s() {
        Long memberId = MicroAuthContext.getMemberId();
        MemberRole queryMemberRole = new MemberRole();
        queryMemberRole.setMemberId(memberId);
        List<MemberRole> memberRoleList = memberRoleService.list(Wrappers.query(queryMemberRole));
        if (CollectionUtils.isNotEmpty(memberRoleList)) {
            memberRoleList.stream().map(MemberRole::getRoleId);
        }
    }

}
