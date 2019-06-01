package cn.micro.biz.service.member;

import cn.micro.biz.commons.auth.MicroToken;
import cn.micro.biz.commons.mybatis.extension.IMicroService;
import cn.micro.biz.entity.member.Account;
import cn.micro.biz.model.add.RegisterAccount;
import cn.micro.biz.model.edit.ChangePassword;
import cn.micro.biz.model.edit.ForgetPassword;
import cn.micro.biz.model.query.LoginAccount;
import cn.micro.biz.pubsrv.wx.WxAuthCode2Session;

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

    /**
     * 微信代理登录
     *
     * @param code     js_code
     * @param register true表示自动注册
     * @return {@link WxAuthCode2Session}
     */
    WxAuthCode2Session wxLogin(String code, boolean register);

    /**
     * 刷新Token
     *
     * @return {@link MicroToken}
     */
    MicroToken doRefreshToken();

    /**
     * 忘记密码-修改密码
     *
     * @param forgetPassword {@link ForgetPassword}
     * @return true表示修改成功
     */
    Boolean doForgetPassword(ForgetPassword forgetPassword);

    /**
     * 修改密码
     *
     * @param changePassword {@link ChangePassword}
     * @return true表示修改成功
     */
    Boolean doChangePassword(ChangePassword changePassword);

}
