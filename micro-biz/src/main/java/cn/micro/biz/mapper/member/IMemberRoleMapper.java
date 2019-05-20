package cn.micro.biz.mapper.member;

import cn.micro.biz.entity.member.MemberRole;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;

/**
 * 会员-角色 Mapper 接口
 *
 * @author lry
 */
public interface IMemberRoleMapper extends BaseMapper<MemberRole> {

    List<String> selectRoleCodesByMemberId(Long memberId);

}