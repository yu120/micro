package cn.micro.biz.pubsrv.pay;

import cn.micro.biz.entity.order.OrdersEntity;
import org.springframework.stereotype.Service;

@Service
public class PayOrderService {

    public OrdersEntity selectPayOrderByOrderNo(String orderNo) {
        return null;
    }

    public boolean updatePayOrderById(OrdersEntity order) {
        return true;
    }

}
