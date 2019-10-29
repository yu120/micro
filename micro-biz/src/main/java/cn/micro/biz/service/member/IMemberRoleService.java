package cn.micro.biz.service.member;

import cn.micro.biz.entity.member.MemberEntity;
import cn.micro.biz.entity.member.MemberRoleEntity;
import cn.micro.biz.commons.mybatis.extension.IMicroService;

import java.util.List;

/**
 * Member Role Service
 *
 * @author lry
 */
public interface IMemberRoleService extends IMicroService<MemberRoleEntity> {

    /**
     * 查询某个用户的所有角色CODE列表
     *
     * @param memberId {@link MemberEntity#id}
     * @return role code list
     */
    List<String> queryMemberRoles(Long memberId);

}
