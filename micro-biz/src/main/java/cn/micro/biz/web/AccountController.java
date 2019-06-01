package cn.micro.biz.web;

import cn.micro.biz.commons.auth.MicroToken;
import cn.micro.biz.commons.auth.PreAuth;
import cn.micro.biz.model.add.RegisterAccount;
import cn.micro.biz.model.edit.ChangePassword;
import cn.micro.biz.model.edit.ForgetPassword;
import cn.micro.biz.model.query.LoginAccount;
import cn.micro.biz.pubsrv.wx.WxAuthCode2Session;
import cn.micro.biz.service.member.IAccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * Account Controller
 *
 * @author lry
 */
@Validated
@RestController
@RequestMapping("account")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class AccountController {

    private final IAccountService accountService;

    @RequestMapping(value = "register", method = RequestMethod.PUT)
    public Boolean register(@RequestBody @Validated RegisterAccount registerAccount) {
        return accountService.doRegister(registerAccount);
    }

    @RequestMapping(value = "login", method = RequestMethod.POST)
    public MicroToken login(@RequestBody @Validated LoginAccount loginAccount) {
        return accountService.doLogin(loginAccount);
    }

    /**
     * 微信自动登录常用方式（账号和密码都是unionid）：
     * <p>
     * 方式一：使用静默授权直接获取 unionid，然后分配账号和用户信息，当进行其他操作时，强制要求绑定手机号或邮箱地址
     * 方式二：获取unionid，然后获取用户信息，然后自动注册，然后自动登录
     * <p>
     * 微信自动登录流程:
     * <p>
     * 1.使用 GET https://open.weixin.qq.com/connect/oauth2/authorize 获取CODE
     * 2.使用 GET /account/wx 获取openid
     * 3.上一步返回 hasAccount=false, 则使用 PUT /account/register 进行注册；否则使用 POST /account/login 进行登录
     * <p>
     * {@see https://developers.weixin.qq.com/miniprogram/dev/framework/open-ability/login.html}
     * {@see https://developers.weixin.qq.com/community/develop/doc/0006026b3c83c0e244573a0025bc08}
     *
     * @param code     code
     * @param register 默认为true,表示会自动注册账号和用户信息
     * @return {@link WxAuthCode2Session}
     */
    @RequestMapping(value = "wx", method = RequestMethod.GET)
    public WxAuthCode2Session wxLogin(
            @RequestParam("code") String code,
            @RequestParam(value = "register", required = false, defaultValue = "true") Boolean register) {
        return accountService.wxLogin(code, register);
    }

    @RequestMapping(value = "forget-password", method = RequestMethod.POST)
    public Boolean forgetPassword(@RequestBody @Validated ForgetPassword forgetPassword) {
        return accountService.doForgetPassword(forgetPassword);
    }

    @PreAuth
    @RequestMapping(value = "change-password", method = RequestMethod.POST)
    public Boolean changePassword(@RequestBody @Validated ChangePassword changePassword) {
        return accountService.doChangePassword(changePassword);
    }

}
