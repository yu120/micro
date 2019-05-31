package cn.micro.biz.web;

import cn.micro.biz.commons.auth.NonAuth;
import cn.micro.biz.commons.auth.PreAuth;
import cn.micro.biz.model.vo.OssTokenVO;
import cn.micro.biz.pubsrv.oss.QiNiuOssService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * OSS Controller
 *
 * @author lry
 */
@PreAuth
@Validated
@RestController
@RequestMapping("oss")
public class OssController {

    @Resource
    private QiNiuOssService qiNiuOssService;

    @NonAuth
    @RequestMapping(value = "token", method = RequestMethod.GET)
    public OssTokenVO token() {
        return qiNiuOssService.uploadToken();
    }

}
