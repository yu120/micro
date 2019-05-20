package cn.micro.biz.web;

import cn.micro.biz.commons.auth.PreAuth;
import cn.micro.biz.pubsrv.sms.AliYunSmsService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * SMS Controller
 *
 * @author lry
 */
@PreAuth
@Validated
@RestController
@RequestMapping("sms")
public class SmsController {

    @Resource
    private AliYunSmsService aliYunSmsService;

    @RequestMapping(value = "send", method = RequestMethod.GET)
    public Boolean send() throws Exception {
        return aliYunSmsService.send("SMS_151232xxx", "15888888888", "李景枫", "电脑", "10001");
    }

}
