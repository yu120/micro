package cn.micro.biz.service.member;

import cn.micro.biz.commons.mybatis.extension.IMicroService;
import cn.micro.biz.entity.member.Role;

import java.util.List;

/**
 * Role Service
 *
 * @author lry
 */
public interface IRoleService extends IMicroService<Role> {

    List<Role> roles();

}
