package cn.micro.biz.commons.code;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum MicroColumnType {

    // ===

    DELETED("DELETED", "`%s` tinyint(1) UNSIGNED ZEROFILL NULL DEFAULT 0 COMMENT '%s',"),
    CREATED("CREATED", "`%s` timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT '%s',"),
    EDITED("EDITED", "`%s` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '%s',");

    private String javaType;
    private String sqlType;

    public static String parse(String javaType) {
        for (MicroColumnType e : values()) {
            if (e.getJavaType().equals(javaType)) {
                return e.getSqlType();
            }
        }

        throw new IllegalArgumentException("未知类型");
    }

}
