package cn.micro.biz.service.goods.impl;

import cn.micro.biz.commons.mybatis.extension.MicroServiceImpl;
import cn.micro.biz.entity.goods.GoodsAttributeEntity;
import cn.micro.biz.mapper.goods.IGoodsAttributeMapper;
import cn.micro.biz.service.goods.IGoodsAttributeService;
import org.springframework.stereotype.Service;

/**
 * Goods Attribute Service Implements
 *
 * @author lry
 */
@Service
public class GoodsAttributeServiceImpl extends MicroServiceImpl<IGoodsAttributeMapper,
        GoodsAttributeEntity> implements IGoodsAttributeService {

}
