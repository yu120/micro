package cn.micro.biz.web;

import cn.micro.biz.commons.auth.MicroAuthContext;
import cn.micro.biz.commons.auth.PreAuth;
import cn.micro.biz.entity.member.Member;
import cn.micro.biz.service.member.IMemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
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
        Long memberId = MicroAuthContext.getMemberId();
        Member member = memberService.getById(memberId);
        member.desensitization();
        return member;
    }

}
