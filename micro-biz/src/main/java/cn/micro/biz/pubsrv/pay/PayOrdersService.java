package cn.micro.biz.pubsrv.pay;

import cn.micro.biz.entity.orders.OrdersEntity;
import org.springframework.stereotype.Service;

@Service
public class PayOrdersService {

    public OrdersEntity selectPayOrderByOrderNo(String orderNo) {
        return null;
    }

    public boolean updatePayOrderById(OrdersEntity order) {
        return true;
    }

}
