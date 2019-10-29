package cn.micro.biz.service.orders.impl;

import cn.micro.biz.commons.mybatis.extension.MicroServiceImpl;
import cn.micro.biz.entity.orders.OrdersReturnEntity;
import cn.micro.biz.mapper.orders.IOrdersReturnMapper;
import cn.micro.biz.service.orders.IOrdersReturnsService;
import org.springframework.stereotype.Service;

/**
 * Order Returns Service Implements
 *
 * @author lry
 */
@Service
public class OrdersReturnsServiceImpl extends MicroServiceImpl<IOrdersReturnMapper, OrdersReturnEntity> implements IOrdersReturnsService {

}
