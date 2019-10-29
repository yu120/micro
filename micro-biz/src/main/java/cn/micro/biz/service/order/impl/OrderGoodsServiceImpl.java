package cn.micro.biz.service.order.impl;

import cn.micro.biz.commons.mybatis.extension.MicroServiceImpl;
import cn.micro.biz.entity.order.OrderGoodsEntity;
import cn.micro.biz.mapper.order.IOrderGoodsMapper;
import cn.micro.biz.service.order.IOrderGoodsService;
import org.springframework.stereotype.Service;

/**
 * Order Goods Service Implements
 *
 * @author lry
 */
@Service
public class OrderGoodsServiceImpl extends MicroServiceImpl<IOrderGoodsMapper, OrderGoodsEntity> implements IOrderGoodsService {

}
