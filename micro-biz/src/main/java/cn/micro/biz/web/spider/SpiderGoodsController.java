package cn.micro.biz.web.spider;

import cn.micro.biz.commons.auth.NonAuth;
import cn.micro.biz.commons.auth.PreAuth;
import cn.micro.biz.service.spider.ISpiderGoodsService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * Spider Goods Controller
 *
 * @author lry
 */
@PreAuth
@Validated
@RestController
@RequestMapping("spider_goods")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class SpiderGoodsController {

    private final ISpiderGoodsService spiderGoodsService;

    @NonAuth
    @RequestMapping(value = "spider",method = RequestMethod.POST)
    public Boolean spider(@RequestParam("appCategory") Integer appCategory) {
        return spiderGoodsService.spider(appCategory);
    }

}
