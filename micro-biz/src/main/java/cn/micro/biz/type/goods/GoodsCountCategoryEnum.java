package cn.micro.biz.type.goods;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Goods Count Category
 *
 * @author lry
 */
@Getter
@AllArgsConstructor
public enum GoodsCountCategoryEnum {

    // ======

    EXPOSURE(3, "曝光量"),
    SUPPORT(4, "点赞数"),
    TRANSMIT(5, "转发量"),
    COLLECT(6, "收藏数"),
    SALE(7, "销售量"),
    ;

    private final int value;
    private final String title;

}
