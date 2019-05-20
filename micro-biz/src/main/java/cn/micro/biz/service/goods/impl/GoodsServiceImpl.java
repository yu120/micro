package cn.micro.biz.service.goods.impl;

import cn.micro.biz.commons.mybatis.extension.MicroServiceImpl;
import cn.micro.biz.entity.goods.Goods;
import cn.micro.biz.mapper.goods.IGoodsMapper;
import cn.micro.biz.service.goods.IGoodsService;
import org.springframework.stereotype.Service;

@Service
public class GoodsServiceImpl extends MicroServiceImpl<IGoodsMapper, Goods> implements IGoodsService {

}
