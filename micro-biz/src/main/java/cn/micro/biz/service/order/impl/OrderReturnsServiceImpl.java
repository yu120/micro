package cn.micro.biz.service.order.impl;

import cn.micro.biz.commons.mybatis.extension.MicroServiceImpl;
import cn.micro.biz.entity.order.OrdersReturnEntity;
import cn.micro.biz.mapper.order.IOrderReturnMapper;
import cn.micro.biz.service.order.IOrderReturnsService;
import org.springframework.stereotype.Service;

/**
 * Order Returns Service Implements
 *
 * @author lry
 */
@Service
public class OrderReturnsServiceImpl extends MicroServiceImpl<IOrderReturnMapper, OrdersReturnEntity> implements IOrderReturnsService {

}
