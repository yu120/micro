package cn.micro.biz.pubsrv.pay;

import cn.micro.biz.entity.order.OrderEntity;
import org.springframework.stereotype.Service;

@Service
public class PayOrderService {

    public OrderEntity selectPayOrderByOrderNo(String orderNo) {
        return null;
    }

    public boolean updatePayOrderById(OrderEntity order) {
        return true;
    }

}
