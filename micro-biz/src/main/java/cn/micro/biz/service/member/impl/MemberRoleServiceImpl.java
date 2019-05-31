package cn.micro.biz.service.member.impl;

import cn.micro.biz.entity.member.MemberRole;
import cn.micro.biz.mapper.member.IMemberGroupMemberMapper;
import cn.micro.biz.mapper.member.IMemberRoleMapper;
import cn.micro.biz.commons.mybatis.extension.MicroServiceImpl;
import cn.micro.biz.service.member.IMemberRoleService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * Member Role Service Implements
 *
 * @author lry
 */
@Service
public class MemberRoleServiceImpl extends MicroServiceImpl<IMemberRoleMapper, MemberRole> implements IMemberRoleService {

    @Resource
    private IMemberGroupMemberMapper memberGroupMemberMapper;

    @Override
    public List<String> queryMemberRoles(Long memberId) {
        List<String> roleCodes = new ArrayList<>();

        // 查询用户直接拥有的角色CODE列表
        List<String> memberRoleCodeList = baseMapper.selectRoleCodesByMemberId(memberId);
        if (memberRoleCodeList != null && !memberRoleCodeList.isEmpty()) {
            roleCodes.addAll(memberRoleCodeList);
        }

        // 查询用户所属用户组拥有的角色CODE列表
        List<String> memberGroupRoleCodeList = memberGroupMemberMapper.selectRoleCodesByMemberId(memberId);
        if (memberGroupRoleCodeList != null && !memberGroupRoleCodeList.isEmpty()) {
            roleCodes.addAll(memberGroupRoleCodeList);
        }

        return roleCodes;
    }

}
