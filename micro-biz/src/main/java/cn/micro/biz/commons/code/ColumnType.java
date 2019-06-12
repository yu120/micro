package cn.micro.biz.commons.code;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.apache.commons.lang3.StringUtils;

@Getter
@AllArgsConstructor
public enum ColumnType {

    // ===

    STRING("java.lang.String", "varchar(100)", "CHARACTER SET utf8 COLLATE utf8_general_ci"),
    LONG("java.lang.Long", "bigint(20)", null),
    INTEGER("java.lang.Integer", "int(11)", null),
    ENUM("java.lang.Enum", "tinyint(1)", "UNSIGNED ZEROFILL"),
    TIMESTAMP("java.sql.Timestamp", "timestamp", "NULL");

    private String javaType;
    private String sqlType;
    private String otherType;

    public static final String NEWLINE = "\n";
    public static final String JAVA_ENUM = "java.lang.Enum";
    public static final String PRIMARY_KEY_SQL_COLUMN = " NOT NULL AUTO_INCREMENT";
    public static final String PRIMARY_KEY = "PRIMARY KEY (`%s`) USING BTREE";
    public static final String TABLE_SQL_PREFIX = "DROP TABLE IF EXISTS `%s`;\nCREATE TABLE `%s` (\n";
    public static final String TABLE_SQL_COLUMN = "`%s` %s COMMENT '%s',";
    public static final String TABLE_SQL_SUFFIX = "\n) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 " +
            "COLLATE = utf8_general_ci COMMENT = '%s' ROW_FORMAT = Dynamic;";

    public static String parse(String javaType) {
        for (ColumnType e : values()) {
            if (e.getJavaType().equals(javaType)) {
                if (StringUtils.isNotBlank(e.getOtherType())) {
                    return e.getSqlType() + " " + e.getOtherType();
                } else {
                    return e.getSqlType();
                }
            }
        }

        throw new IllegalArgumentException("未知类型");
    }

    public static String parse(String serial, String javaType) {
        for (ColumnType e : values()) {
            if (e.getJavaType().equals(javaType)) {
                if (StringUtils.isNotBlank(e.getOtherType())) {
                    return serial + " " + e.getOtherType();
                } else {
                    return serial;
                }
            }
        }

        throw new IllegalArgumentException("未知类型");
    }

}
