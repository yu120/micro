package cn.micro.biz.service.order.impl;

import cn.micro.biz.commons.mybatis.extension.MicroServiceImpl;
import cn.micro.biz.entity.order.Order;
import cn.micro.biz.mapper.order.IOrderMapper;
import cn.micro.biz.service.order.IOrderService;
import org.springframework.stereotype.Service;

@Service
public class OrderServiceImpl extends MicroServiceImpl<IOrderMapper, Order> implements IOrderService {

}
