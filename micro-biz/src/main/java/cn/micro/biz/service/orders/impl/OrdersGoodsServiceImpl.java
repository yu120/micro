package cn.micro.biz.service.orders.impl;

import cn.micro.biz.entity.orders.OrdersGoodsEntity;
import cn.micro.biz.mapper.orders.IOrdersGoodsMapper;
import cn.micro.biz.service.orders.IOrdersGoodsService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * Order Goods Service Implements
 *
 * @author lry
 */
@Service
public class OrdersGoodsServiceImpl extends ServiceImpl<IOrdersGoodsMapper, OrdersGoodsEntity> implements IOrdersGoodsService {

}
