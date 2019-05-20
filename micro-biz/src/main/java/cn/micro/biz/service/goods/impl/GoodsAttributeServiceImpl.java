package cn.micro.biz.service.goods.impl;

import cn.micro.biz.commons.mybatis.extension.MicroServiceImpl;
import cn.micro.biz.entity.goods.GoodsAttribute;
import cn.micro.biz.mapper.goods.IGoodsAttributeMapper;
import cn.micro.biz.service.goods.IGoodsAttributeService;
import org.springframework.stereotype.Service;

@Service
public class GoodsAttributeServiceImpl extends MicroServiceImpl<IGoodsAttributeMapper, GoodsAttribute> implements IGoodsAttributeService {

}
