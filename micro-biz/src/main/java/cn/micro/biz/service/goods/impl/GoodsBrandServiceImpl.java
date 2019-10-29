package cn.micro.biz.service.goods.impl;

import cn.micro.biz.commons.mybatis.extension.MicroServiceImpl;
import cn.micro.biz.entity.goods.GoodsBrandEntity;
import cn.micro.biz.mapper.goods.IGoodsBrandMapper;
import cn.micro.biz.service.goods.IGoodsBrandService;
import org.springframework.stereotype.Service;

/**
 * Goods Brand Service Implements
 *
 * @author lry
 */
@Service
public class GoodsBrandServiceImpl extends MicroServiceImpl<IGoodsBrandMapper,
        GoodsBrandEntity> implements IGoodsBrandService {

}
