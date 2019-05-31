package cn.micro.biz.mapper.member;

import cn.micro.biz.commons.mybatis.extension.IMicroMapper;
import cn.micro.biz.entity.member.MemberGroupMember;

import java.util.List;

/**
 * Member Group to Member Mapper
 *
 * @author lry
 */
public interface IMemberGroupMemberMapper extends IMicroMapper<MemberGroupMember> {

    /**
     * 查询用户所属用户组的角色CODE列表
     *
     * @param memberId
     * @return
     */
    List<String> selectRoleCodesByMemberId(Long memberId);

}