package cn.micro.biz.mapper.member;

import cn.micro.biz.entity.member.MemberRole;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;

/**
 * Member Role Mapper
 *
 * @author lry
 */
public interface IMemberRoleMapper extends BaseMapper<MemberRole> {

    List<String> selectRoleCodesByMemberId(Long memberId);

}