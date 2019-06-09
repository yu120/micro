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

    WEB(1, "前端"),
    ADMIN(2, "后台"),
    WX(3, "微信"),
    ALI(4, "支付宝");

    private final Integer value;
    private final String title;

}
