package cn.micro.biz.web;

import cn.micro.biz.commons.auth.MicroToken;
import cn.micro.biz.model.add.RegisterMemberAdd;
import cn.micro.biz.model.query.LoginAccountQuery;
import cn.micro.biz.service.member.IAccountService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * 账号 前端控制器
 *
 * @author lry
 */
@Validated
@RestController
@RequestMapping("account")
public class AccountController {

    @Resource
    private IAccountService accountService;

    @RequestMapping(value = "register", method = RequestMethod.POST)
    public Boolean register(@RequestBody @Validated RegisterMemberAdd registerMemberAdd) throws Exception {
        return accountService.addRegister(registerMemberAdd);
    }

    @RequestMapping(value = "login", method = RequestMethod.POST)
    public MicroToken login(@RequestBody @Validated LoginAccountQuery loginAccountQuery) throws Exception {
        return accountService.queryLogin(loginAccountQuery);
    }

}
