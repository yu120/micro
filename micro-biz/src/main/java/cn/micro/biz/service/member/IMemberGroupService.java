package cn.micro.biz.service.member;

import cn.micro.biz.commons.mybatis.extension.IMicroService;
import cn.micro.biz.entity.member.MemberGroup;

import java.util.List;

/**
 * Member Group Service
 *
 * @author lry
 */
public interface IMemberGroupService extends IMicroService<MemberGroup> {

    List<MemberGroup> memberGroups();

}
