package cn.micro.biz.service.spider;

import cn.micro.biz.commons.mybatis.extension.IMicroService;
import cn.micro.biz.entity.spider.SpiderGoods;

/**
 * Spider Goods Service
 *
 * @author lry
 */
public interface ISpiderGoodsService extends IMicroService<SpiderGoods> {

    Boolean spider(Integer appCategory);

}
