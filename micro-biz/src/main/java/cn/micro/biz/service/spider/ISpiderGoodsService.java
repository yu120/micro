package cn.micro.biz.service.spider;

import cn.micro.biz.commons.mybatis.extension.IMicroService;
import cn.micro.biz.entity.spider.SpiderGoodsEntity;

/**
 * Spider Goods Service
 *
 * @author lry
 */
public interface ISpiderGoodsService extends IMicroService<SpiderGoodsEntity> {

    Boolean spider(Integer appCategory);

}
