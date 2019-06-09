package cn.micro.biz.type.unified;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 逻辑删除
 *
 * @author lry
 */
@Getter
@AllArgsConstructor
public enum DeletedEnum {

    // ======

    UN_DELETE(0, "未删除"),
    DELETED(1, "已删除");

    private final int value;
    private final String title;

}
