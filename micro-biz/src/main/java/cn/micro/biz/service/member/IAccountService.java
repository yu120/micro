package cn.micro.biz.service.member;

import cn.micro.biz.commons.auth.MicroToken;
import cn.micro.biz.commons.mybatis.extension.IMicroService;
import cn.micro.biz.entity.member.Account;
import cn.micro.biz.model.add.RegisterMemberAdd;
import cn.micro.biz.model.query.LoginAccountQuery;

/**
 * Account Service
 *
 * @author lry
 */
public interface IAccountService extends IMicroService<Account> {

    /**
     * 通用账号注册
     *
     * @param registerMemberAdd {@link RegisterMemberAdd}
     * @return true register success
     */
    Boolean addRegister(RegisterMemberAdd registerMemberAdd) throws Exception;

    /**
     * 通用账号登录
     *
     * @param loginAccountQuery {@link LoginAccountQuery}
     * @return {@link MicroToken}
     */
    MicroToken queryLogin(LoginAccountQuery loginAccountQuery) throws Exception;

}
