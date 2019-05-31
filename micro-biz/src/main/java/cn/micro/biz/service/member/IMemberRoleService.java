package cn.micro.biz.service.member;

import cn.micro.biz.entity.member.MemberRole;
import cn.micro.biz.commons.mybatis.extension.IMicroService;

import java.util.List;

/**
 * Member Role Service
 *
 * @author lry
 */
public interface IMemberRoleService extends IMicroService<MemberRole> {

    /**
     * 查询某个用户的所有角色CODE列表
     *
     * @param memberId {@link cn.micro.biz.entity.member.Member#id}
     * @return role code list
     */
    List<String> queryMemberRoles(Long memberId);

}
