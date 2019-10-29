package cn.micro.biz.type.action;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

/**
 * 目标类型
 *
 * @author lry
 */
@Getter
@ToString
@AllArgsConstructor
public enum TargetTypeEnum {

    // ===

    NEWS(0, "新闻");

    private final int value;
    private final String title;

}
