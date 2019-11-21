package cn.micro.biz.service.orders.impl;

import cn.micro.biz.entity.orders.OrdersReturnEntity;
import cn.micro.biz.mapper.orders.IOrdersReturnMapper;
import cn.micro.biz.service.orders.IOrdersReturnsService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * Order Returns Service Implements
 *
 * @author lry
 */
@Service
public class OrdersReturnsServiceImpl extends ServiceImpl<IOrdersReturnMapper, OrdersReturnEntity> implements IOrdersReturnsService {

}
