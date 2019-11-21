package cn.micro.biz.mapper.member;

import cn.micro.biz.entity.member.MemberRoleEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;

/**
 * Member Role Mapper
 *
 * @author lry
 */
public interface IMemberRoleMapper extends BaseMapper<MemberRoleEntity> {

    /**
     * 查询用户直接拥有的角色CODE列表
     *
     * @param memberId
     * @return
     */
    List<String> selectRoleCodesByMemberId(Long memberId);

}