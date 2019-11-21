package cn.micro.biz.service.spider;

import cn.micro.biz.entity.spider.SpiderGoodsEntity;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * Spider Goods Service
 *
 * @author lry
 */
public interface ISpiderGoodsService extends IService<SpiderGoodsEntity> {

    Boolean spider(Integer appCategory);

}
