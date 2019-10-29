package cn.micro.biz.service.goods.impl;

import cn.micro.biz.commons.mybatis.extension.MicroServiceImpl;
import cn.micro.biz.entity.goods.GoodsEntity;
import cn.micro.biz.mapper.goods.IGoodsMapper;
import cn.micro.biz.service.goods.IGoodsService;
import org.springframework.stereotype.Service;

/**
 * Goods Service Implements
 *
 * @author lry
 */
@Service
public class GoodsServiceImpl extends MicroServiceImpl<IGoodsMapper, GoodsEntity> implements IGoodsService {

}
