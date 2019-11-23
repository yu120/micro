package cn.micro.biz.pubsrv.alarm;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * AlarmLevel
 *
 * @author lry
 */
@Getter
@AllArgsConstructor
public enum AlarmLevel {

    // ===

    PROMPT(0, "提示"),
    WARNING(1, "警告"),
    SERIOUS(2, "严重");

    private final int code;
    private final String msg;

}
