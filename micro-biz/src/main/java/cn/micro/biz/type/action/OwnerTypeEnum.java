package cn.micro.biz.type.action;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

/**
 * 点赞发起者类型
 *
 * @author lry
 */
@Getter
@ToString
@AllArgsConstructor
public enum OwnerTypeEnum {

    // ===

    USER(0, "一般用户");

    private final int value;
    private final String title;

}
