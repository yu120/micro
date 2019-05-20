package cn.micro.biz.service.member.impl;

import cn.micro.biz.commons.mybatis.extension.MicroServiceImpl;
import cn.micro.biz.entity.member.Member;
import cn.micro.biz.mapper.member.IMemberMapper;
import cn.micro.biz.service.member.IMemberService;
import org.springframework.stereotype.Service;

/**
 * 用户信息 服务实现类
 *
 * @author lry
 */
@Service
public class MemberServiceImpl extends MicroServiceImpl<IMemberMapper, Member> implements IMemberService {

}
