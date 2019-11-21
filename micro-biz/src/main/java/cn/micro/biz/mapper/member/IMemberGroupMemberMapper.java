package cn.micro.biz.mapper.member;

import cn.micro.biz.entity.member.MemberGroupMemberEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;

/**
 * Member Group to Member Mapper
 *
 * @author lry
 */
public interface IMemberGroupMemberMapper extends BaseMapper<MemberGroupMemberEntity> {

    /**
     * 查询用户所属用户组的角色CODE列表
     *
     * @param memberId
     * @return
     */
    List<String> selectRoleCodesByMemberId(Long memberId);

}