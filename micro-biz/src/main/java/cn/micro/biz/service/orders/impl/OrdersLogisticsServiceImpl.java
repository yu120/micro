package cn.micro.biz.service.orders.impl;

import cn.micro.biz.entity.orders.OrdersLogisticsEntity;
import cn.micro.biz.mapper.orders.IOrdersLogisticsMapper;
import cn.micro.biz.service.orders.IOrdersLogisticsService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * Order Logistics Service Implements
 *
 * @author lry
 */
@Service
public class OrdersLogisticsServiceImpl extends ServiceImpl<IOrdersLogisticsMapper, OrdersLogisticsEntity> implements IOrdersLogisticsService {

}
