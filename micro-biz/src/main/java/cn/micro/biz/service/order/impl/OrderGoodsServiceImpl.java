package cn.micro.biz.service.order.impl;

import cn.micro.biz.commons.mybatis.extension.MicroServiceImpl;
import cn.micro.biz.entity.order.OrderGoods;
import cn.micro.biz.mapper.order.IOrderGoodsMapper;
import cn.micro.biz.service.order.IOrderGoodsService;
import org.springframework.stereotype.Service;

@Service
public class OrderGoodsServiceImpl extends MicroServiceImpl<IOrderGoodsMapper, OrderGoods> implements IOrderGoodsService {

}
