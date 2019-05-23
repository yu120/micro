package cn.micro.biz.service.member;

import cn.micro.biz.commons.mybatis.extension.IMicroService;
import cn.micro.biz.entity.member.Permission;

import java.util.List;

/**
 * Permission Service
 *
 * @author lry
 */
public interface IPermissionService extends IMicroService<Permission> {

    List<Permission> permissions();

}
