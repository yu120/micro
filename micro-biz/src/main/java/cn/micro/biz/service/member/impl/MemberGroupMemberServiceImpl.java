package cn.micro.biz.service.member.impl;

import cn.micro.biz.commons.mybatis.extension.MicroServiceImpl;
import cn.micro.biz.entity.member.MemberGroupMember;
import cn.micro.biz.mapper.member.IMemberGroupMemberMapper;
import cn.micro.biz.service.member.IMemberGroupMemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Member Group Member Service Implements
 *
 * @author lry
 */
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class MemberGroupMemberServiceImpl extends MicroServiceImpl<IMemberGroupMemberMapper, MemberGroupMember> implements IMemberGroupMemberService {

}
