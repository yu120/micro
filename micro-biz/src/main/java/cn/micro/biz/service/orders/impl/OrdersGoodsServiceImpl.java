package cn.micro.biz.service.orders.impl;

import cn.micro.biz.commons.mybatis.extension.MicroServiceImpl;
import cn.micro.biz.entity.orders.OrdersGoodsEntity;
import cn.micro.biz.mapper.orders.IOrdersGoodsMapper;
import cn.micro.biz.service.orders.IOrdersGoodsService;
import org.springframework.stereotype.Service;

/**
 * Order Goods Service Implements
 *
 * @author lry
 */
@Service
public class OrdersGoodsServiceImpl extends MicroServiceImpl<IOrdersGoodsMapper, OrdersGoodsEntity> implements IOrdersGoodsService {

}
