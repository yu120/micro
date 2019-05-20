package cn.micro.biz.web;

import cn.micro.biz.commons.auth.PreAuth;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * Member Controller
 *
 * @author lry
 */
@PreAuth
@Validated
@RestController
@RequestMapping("member")
public class MemberController {

}
