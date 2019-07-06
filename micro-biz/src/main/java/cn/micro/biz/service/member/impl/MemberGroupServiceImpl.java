package cn.micro.biz.service.member.impl;

import cn.micro.biz.commons.mybatis.extension.MicroServiceImpl;
import cn.micro.biz.entity.member.MemberGroup;
import cn.micro.biz.mapper.member.IMemberGroupMapper;
import cn.micro.biz.service.member.IMemberGroupService;
import org.springframework.stereotype.Service;

/**
 * Member Group Service Implements
 *
 * @author lry
 */
@Service
public class MemberGroupServiceImpl extends MicroServiceImpl<IMemberGroupMapper, MemberGroup> implements IMemberGroupService {

}
