package cn.micro.biz.service.orders.impl;

import cn.micro.biz.entity.orders.OrdersEntity;
import cn.micro.biz.mapper.orders.IOrdersMapper;
import cn.micro.biz.service.orders.IOrdersService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * Order Service Implements
 *
 * @author lry
 */
@Service
public class OrdersServiceImpl extends ServiceImpl<IOrdersMapper, OrdersEntity> implements IOrdersService {

}
