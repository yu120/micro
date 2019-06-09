package cn.micro.biz.type.unified;

import cn.micro.biz.commons.enums.IEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

/**
 * 逻辑删除
 *
 * @author lry
 */
@Getter
@ToString
@AllArgsConstructor
public enum DeletedEnum implements IEnum<Integer> {

    // ======

    UN_DELETE(0, "未删除"),
    DELETED(1, "已删除");

    private final Integer value;
    private final String title;

}
