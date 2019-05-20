package cn.micro.biz.pubsrv.pay;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum PayChannelEnum {

    // ====

    ALIPAY(0, "支付宝"),
    /**
     * 微信浏览器拉起微信支付
     */
    WECHAT(1, "微信"),
    /**
     * 系统浏览器拉起微信支付
     */
    WECHAT_H5(2, "微信H5");

    private int status;
    private String message;

    public static PayChannelEnum getPayBy(Integer status) {
        if (status == null) {
            return null;
        }

        for (PayChannelEnum e : values()) {
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

        for (PayChannelEnum e : values()) {
            if (e.getStatus() == status) {
                return true;
            }
        }

        return false;
    }

}
