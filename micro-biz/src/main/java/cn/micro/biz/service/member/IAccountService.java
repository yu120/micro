package cn.micro.biz.service.member;

import cn.micro.biz.commons.auth.MicroToken;
import cn.micro.biz.commons.mybatis.extension.IMicroService;
import cn.micro.biz.entity.member.Account;
import cn.micro.biz.model.add.RegisterAccount;
import cn.micro.biz.model.query.LoginAccount;

/**
 * Account Service
 *
 * @author lry
 */
public interface IAccountService extends IMicroService<Account> {

    /**
     * 通用账号注册
     *
     * @param registerAccount {@link RegisterAccount}
     * @return true register success
     */
    Boolean doRegister(RegisterAccount registerAccount);

    /**
     * 通用账号登录
     *
     * @param loginAccount {@link LoginAccount}
     * @return {@link MicroToken}
     */
    MicroToken doLogin(LoginAccount loginAccount);

}
