package cn.micro.biz.service.goods.impl;

import cn.micro.biz.commons.mybatis.extension.MicroServiceImpl;
import cn.micro.biz.entity.goods.GoodsSpecificationEntity;
import cn.micro.biz.mapper.goods.IGoodsSpecificationMapper;
import cn.micro.biz.service.goods.IGoodsSpecificationService;
import org.springframework.stereotype.Service;

/**
 * Goods Specification Service Implements
 *
 * @author lry
 */
@Service
public class GoodsSpecificationServiceImpl extends MicroServiceImpl<IGoodsSpecificationMapper,
        GoodsSpecificationEntity> implements IGoodsSpecificationService {

}
