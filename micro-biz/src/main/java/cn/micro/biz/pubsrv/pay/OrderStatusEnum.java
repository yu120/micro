package cn.micro.biz.pubsrv.pay;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum OrderStatusEnum {

    // =====

    WAIT_SEND_PAY(0, "已下单", "订单已创建,等待发起支付"),
    WAIT_BUYER_PAY(1, "等待付款", "交易创建，等待买家付款"),
    TRADE_CLOSED(2, "超时关闭", "未付款交易超时关闭，或支付完成后全额退款"),
    TRADE_SUCCESS(3, "支付成功", "交易支付成功"),
    TRADE_FINISHED(4, "交易结束", "交易结束，不可退款"),
    // 主要用于微信支付失败场景
    TRADE_FAIL(5, "支付失败", "支付失败");

    private int status;
    private String title;
    private String message;

    public static OrderStatusEnum getPayBy(Integer status) {
        if (status == null) {
            return null;
        }

        for (OrderStatusEnum e : values()) {
            if (e.getStatus() == status) {
                return e;
            }
        }

        return null;
    }

    public static boolean checkStatus(Integer status) {
        if (status == null) {
            return false;
        }

        for (OrderStatusEnum e : values()) {
            if (e.getStatus() == status) {
                return true;
            }
        }

        return false;
    }

}
