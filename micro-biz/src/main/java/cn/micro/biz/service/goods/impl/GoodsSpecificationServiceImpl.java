package cn.micro.biz.service.goods.impl;

import cn.micro.biz.commons.mybatis.extension.MicroServiceImpl;
import cn.micro.biz.entity.goods.GoodsSpecification;
import cn.micro.biz.mapper.goods.IGoodsSpecificationMapper;
import cn.micro.biz.service.goods.IGoodsSpecificationService;
import org.springframework.stereotype.Service;

@Service
public class GoodsSpecificationServiceImpl extends MicroServiceImpl<IGoodsSpecificationMapper, GoodsSpecification> implements IGoodsSpecificationService {

}
