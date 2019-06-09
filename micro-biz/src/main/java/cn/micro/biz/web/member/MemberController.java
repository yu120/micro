package cn.micro.biz.web.member;

import cn.micro.biz.commons.auth.PreAuth;
import cn.micro.biz.entity.member.Member;
import cn.micro.biz.model.edit.EditMemberInfo;
import cn.micro.biz.service.member.IMemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * Member Controller
 *
 * @author lry
 */
@PreAuth
@Validated
@RestController
@RequestMapping("member")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class MemberController {

    private final IMemberService memberService;

    @RequestMapping(value = "info", method = RequestMethod.GET)
    public Member info() {
        return memberService.info();
    }

    @RequestMapping(value = "edit", method = RequestMethod.PUT)
    public Boolean edit(@RequestBody @Validated EditMemberInfo memberInfo) {
        return memberService.edit(memberInfo);
    }

}
