package cn.micro.biz.type.order;

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
public enum OrderStatusEnum implements IEnum<Integer> {

    // ======

    ;

    private final Integer value;
    private final String title;

}
