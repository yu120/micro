package cn.micro.biz.type.order;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Order Status
 *
 * @author lry
 */
@Getter
@AllArgsConstructor
public enum OrderStatusEnum {

    // ======

    ;

    private final int value;
    private final String title;

}
