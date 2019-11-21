package cn.micro.biz.service.orders.impl;

import cn.micro.biz.entity.orders.OrdersInvoiceEntity;
import cn.micro.biz.mapper.orders.IOrdersInvoiceMapper;
import cn.micro.biz.service.orders.IOrdersInvoiceService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * Order Invoice Service Implements
 *
 * @author lry
 */
@Service
public class OrdersInvoiceServiceImpl extends ServiceImpl<IOrdersInvoiceMapper, OrdersInvoiceEntity> implements IOrdersInvoiceService {

}
