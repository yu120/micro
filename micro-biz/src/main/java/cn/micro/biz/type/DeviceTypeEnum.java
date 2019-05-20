package cn.micro.biz.type;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 设备类型
 *
 * @author lry
 */
@Getter
@AllArgsConstructor
public enum DeviceTypeEnum {

    // ======

    WEB(1, "前端"),
    ADMIN(2, "后台"),
    WX(3, "微信"),
    ALI(4, "支付宝");

    private final int value;
    private final String title;

}
