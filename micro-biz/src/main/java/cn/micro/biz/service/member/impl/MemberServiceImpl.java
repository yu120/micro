package cn.micro.biz.service.member.impl;

import cn.micro.biz.commons.mybatis.extension.MicroServiceImpl;
import cn.micro.biz.entity.member.Member;
import cn.micro.biz.mapper.member.IMemberMapper;
import cn.micro.biz.service.member.IMemberService;
import org.springframework.stereotype.Service;

/**
 * Member Service Implements
 *
 * @author lry
 */
@Service
public class MemberServiceImpl extends MicroServiceImpl<IMemberMapper, Member> implements IMemberService {

}
