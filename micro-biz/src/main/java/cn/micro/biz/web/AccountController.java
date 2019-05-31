package cn.micro.biz.web;

import cn.micro.biz.commons.auth.MicroToken;
import cn.micro.biz.model.add.RegisterMember;
import cn.micro.biz.model.query.LoginAccountQuery;
import cn.micro.biz.service.member.IAccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

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
    public Boolean register(@RequestBody @Validated RegisterMember registerMember) throws Exception {
        return accountService.doRegister(registerMember);
    }

    @RequestMapping(value = "login", method = RequestMethod.POST)
    public MicroToken login(@RequestBody @Validated LoginAccountQuery loginAccountQuery) throws Exception {
        return accountService.queryLogin(loginAccountQuery);
    }

}
