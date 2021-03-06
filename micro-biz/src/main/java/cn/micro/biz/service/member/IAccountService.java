package cn.micro.biz.service.member;

import cn.micro.biz.commons.auth.MicroToken;
import cn.micro.biz.entity.member.AccountEntity;
import cn.micro.biz.model.add.RegisterAccount;
import cn.micro.biz.model.edit.ChangeEmailOrMobile;
import cn.micro.biz.model.edit.ChangePassword;
import cn.micro.biz.model.edit.ForgetPassword;
import cn.micro.biz.model.query.LoginAccount;
import cn.micro.biz.pubsrv.wechat.response.WeChatCode2SessionResponse;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * Account Service
 *
 * @author lry
 */
public interface IAccountService extends IService<AccountEntity> {

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
     * @return {@link WeChatCode2SessionResponse}
     */
    WeChatCode2SessionResponse weChatLogin(String code);

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

    /**
     * 修改邮箱地址或手机号码
     *
     * @param changeEmailOrMobile {@link ChangeEmailOrMobile}
     * @return true表示修改成功
     */
    Boolean doChangeAccount(ChangeEmailOrMobile changeEmailOrMobile);

    /**
     * 校验账号是否已被注册
     *
     * @param account email,mobile
     * @return true表示已被占用
     */
    Boolean doRegistered(String account);

}
