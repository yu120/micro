package cn.micro.biz.pubsrv.pay;

import cn.micro.biz.entity.order.Order;
import org.springframework.stereotype.Service;

@Service
public class PayOrderService {

    public Order selectPayOrderByOrderNo(String orderNo) {
        return null;
    }

    public boolean updatePayOrderById(Order order) {
        return true;
    }

}
