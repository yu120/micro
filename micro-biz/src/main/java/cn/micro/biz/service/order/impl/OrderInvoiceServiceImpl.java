package cn.micro.biz.service.order.impl;

import cn.micro.biz.commons.mybatis.extension.MicroServiceImpl;
import cn.micro.biz.entity.order.OrdersInvoiceEntity;
import cn.micro.biz.mapper.order.IOrderInvoiceMapper;
import cn.micro.biz.service.order.IOrderInvoiceService;
import org.springframework.stereotype.Service;

/**
 * Order Invoice Service Implements
 *
 * @author lry
 */
@Service
public class OrderInvoiceServiceImpl extends MicroServiceImpl<IOrderInvoiceMapper, OrdersInvoiceEntity> implements IOrderInvoiceService {

}
