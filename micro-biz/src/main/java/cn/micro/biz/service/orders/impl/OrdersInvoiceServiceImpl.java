package cn.micro.biz.service.orders.impl;

import cn.micro.biz.commons.mybatis.extension.MicroServiceImpl;
import cn.micro.biz.entity.orders.OrdersInvoiceEntity;
import cn.micro.biz.mapper.orders.IOrdersInvoiceMapper;
import cn.micro.biz.service.orders.IOrdersInvoiceService;
import org.springframework.stereotype.Service;

/**
 * Order Invoice Service Implements
 *
 * @author lry
 */
@Service
public class OrdersInvoiceServiceImpl extends MicroServiceImpl<IOrdersInvoiceMapper, OrdersInvoiceEntity> implements IOrdersInvoiceService {

}
