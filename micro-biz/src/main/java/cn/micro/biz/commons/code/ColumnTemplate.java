package cn.micro.biz.commons.code;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ColumnTemplate {

    // ===

    DELETED("DELETED", "`%s` tinyint(1) UNSIGNED ZEROFILL NULL DEFAULT 0 COMMENT '%s',"),
    CREATED("CREATED", "`%s` timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT '%s',"),
    EDITED("EDITED", "`%s` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '%s',");

    private String id;
    private String sql;

    public static String parse(String id) {
        for (ColumnTemplate e : values()) {
            if (e.getId().equals(id)) {
                return e.getSql();
            }
        }

        throw new IllegalArgumentException("未知类型");
    }

}
