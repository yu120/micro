package cn.micro.biz.service.member;

import cn.micro.biz.commons.mybatis.extension.IMicroService;
import cn.micro.biz.entity.UnionCode;

/**
 * Union Code Service
 *
 * @author lry
 */
public interface IUnionCodeService extends IMicroService<UnionCode> {

    boolean sendCodeMail(Integer category, String email);

}
