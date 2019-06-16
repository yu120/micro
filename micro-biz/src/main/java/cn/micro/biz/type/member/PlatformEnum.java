package cn.micro.biz.type.member;

import cn.micro.biz.commons.enums.IEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 平台类型
 *
 * @author lry
 */
@Getter
@AllArgsConstructor
public enum PlatformEnum implements IEnum<Integer> {

    // ======

    WEB(0, "前端"),
    ADMIN(1, "后台"),
    WX(2, "微信"),
    ALI(3, "支付宝");

    private final Integer value;
    private final String title;

}
