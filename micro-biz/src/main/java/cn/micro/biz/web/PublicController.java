package cn.micro.biz.web;

import cn.micro.biz.commons.auth.NonAuth;
import cn.micro.biz.commons.auth.PreAuth;
import cn.micro.biz.model.vo.OssTokenVO;
import cn.micro.biz.pubsrv.oss.QiNiuOssService;
import cn.micro.biz.pubsrv.sms.AliYunSmsService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * Public Service Controller
 *
 * @author lry
 */
@PreAuth
@Validated
@RestController
public class PublicController {

    @Resource
    private QiNiuOssService qiNiuOssService;
    @Resource
    private AliYunSmsService aliYunSmsService;

    @NonAuth
    @RequestMapping(value = "oss/token", method = RequestMethod.GET)
    public OssTokenVO token() {
        return qiNiuOssService.uploadToken();
    }

    @RequestMapping(value = "sms/send", method = RequestMethod.GET)
    public Boolean send() throws Exception {
        return aliYunSmsService.send("SMS_151232xxx", "15888888888", "李景枫", "电脑", "10001");
    }

}
