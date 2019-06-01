package cn.micro.biz.web;

import cn.micro.biz.commons.auth.NonAuth;
import cn.micro.biz.commons.auth.PreAuth;
import cn.micro.biz.commons.response.NonMeta;
import cn.micro.biz.commons.trace.GlobalTraceInterceptor;
import cn.micro.biz.model.vo.OssTokenVO;
import cn.micro.biz.pubsrv.oss.QiNiuOssService;
import cn.micro.biz.pubsrv.sms.AliYunSmsService;
import cn.micro.biz.service.member.IUnionCodeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * Public Service Controller
 *
 * @author lry
 */
@Slf4j
@NonAuth
@Validated
@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class PublicController {

    private final QiNiuOssService qiNiuOssService;
    private final AliYunSmsService aliYunSmsService;
    private final IUnionCodeService unionCodeService;

    @RequestMapping(value = "oss-token", method = RequestMethod.GET)
    public OssTokenVO ossToken() {
        return qiNiuOssService.uploadToken();
    }

    @PreAuth
    @RequestMapping(value = "sms-send", method = RequestMethod.GET)
    public Boolean smsSend() throws Exception {
        return aliYunSmsService.send("SMS_151232xxx", "15888888888", "李景枫", "电脑", "10001");
    }

    @RequestMapping(value = "email-send-code", method = RequestMethod.POST)
    public Boolean sendCodeMail(@RequestParam("category") Integer category, @RequestParam("email") String email) {
        return unionCodeService.sendCodeMail(category, email);
    }

    @NonMeta
    @RequestMapping(value = "trace/{traceId}", method = RequestMethod.GET)
    public String getDump(@PathVariable("traceId") String traceId) {
        if (GlobalTraceInterceptor.getCache() == null) {
            return "No start trace cache.";
        }

        String dump = GlobalTraceInterceptor.getCache().getIfPresent(traceId);
        if (dump == null) {
            return "Not found requestId: " + traceId;
        }

        StringBuilder sb = new StringBuilder();
        for (String lineStr : dump.split("\n")) {
            sb.append(lineStr).append("<br/>");
        }

        return sb.toString();
    }

}
