package cn.micro.biz.type.orders;

import cn.micro.biz.commons.enums.IEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

/**
 * Order Status
 *
 * @author lry
 */
@Getter
@ToString
@AllArgsConstructor
public enum OrdersStatusEnum implements IEnum<Integer> {

    // =====

    WAIT_SEND_PAY(0, "已下单", "订单已创建,等待发起支付"),
    WAIT_BUYER_PAY(1, "等待付款", "交易创建，等待买家付款"),
    TRADE_CLOSED(2, "超时关闭", "未付款交易超时关闭，或支付完成后全额退款"),
    TRADE_SUCCESS(3, "支付成功", "交易支付成功"),
    TRADE_FINISHED(4, "交易结束", "交易结束，不可退款"),
    // 主要用于微信支付失败场景
    TRADE_FAIL(5, "支付失败", "支付失败");

    private Integer value;
    private String title;
    private String message;

    public static OrdersStatusEnum getPayBy(Integer status) {
        if (status == null) {
            return null;
        }

        for (OrdersStatusEnum e : OrdersStatusEnum.values()) {
            if (e.getValue() == (int) status) {
                return e;
            }
        }

        return null;
    }

    public static boolean checkStatus(Integer status) {
        if (status == null) {
            return false;
        }

        for (OrdersStatusEnum e : OrdersStatusEnum.values()) {
            if (e.getValue() == (int) status) {
                return true;
            }
        }

        return false;
    }

}
