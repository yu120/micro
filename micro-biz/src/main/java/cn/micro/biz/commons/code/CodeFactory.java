package cn.micro.biz.commons.code;

import cn.micro.biz.commons.mybatis.MicroEntity;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import org.apache.commons.lang3.StringUtils;
import org.micro.neural.common.utils.ClassUtils;

import java.io.File;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.*;

/**
 * CodeFactory
 *
 * @author lry
 */
public enum CodeFactory {

    // ====

    INSTANCE;

    public static void main(String[] args) {
        String realPath = CodeFactory.class.getResource("/").getPath();
        realPath = realPath.substring(0, realPath.length() - 15) + "src/main/java/";
        String packageName = "cn.micro.biz";
        List<String> sqlList = CodeFactory.INSTANCE.handler(realPath, packageName);
        for (String sql : sqlList) {
            System.out.println(sql);
            System.out.println();
        }
    }

    public List<String> handler(String realPath, String packageName) {
        List<String> sqlList = new ArrayList<>();
        // 读取Class列表
        Set<Class<?>> classSet = ClassUtils.getClasses(packageName);
        // 读取注释文档
        Map<String, MicroDoclet.MicroClassDoc> docMap = getDocument(realPath, classSet);
        for (Class<?> clz : classSet) {
            TableName tableNameAnnotation = clz.getDeclaredAnnotation(TableName.class);
            if (tableNameAnnotation != null) {
                String tableName = tableNameAnnotation.value();
                if (StringUtils.isBlank(tableName)) {
                    tableName = clz.getName();
                }
                tableName = humpToUnderline(tableName);

                MicroDoclet.MicroClassDoc microClassDoc = docMap.get(clz.getName());
                List<Field> fields = sortField(recursionField(new ArrayList<>(), clz));
                String sql = buildSql(microClassDoc, tableName, fields);
                sqlList.add(sql);
            }
        }

        return sqlList;
    }

    private Map<String, MicroDoclet.MicroClassDoc> getDocument(String realPath, Set<Class<?>> classSet) {
        // 读取
        List<String> realPathList = new ArrayList<>();
        realPathList.add(realPath + MicroEntity.class.getName().replace(".", File.separator) + ".java");
        for (Class<?> clz : classSet) {
            TableName tableNameAnnotation = clz.getDeclaredAnnotation(TableName.class);
            if (tableNameAnnotation != null) {
                String path = realPath + clz.getName().replace(".", File.separator) + ".java";
                System.out.println(path);
                realPathList.add(path);
            }
        }

        return MicroDoclet.getDoc(realPathList);
    }

    private String buildSql(MicroDoclet.MicroClassDoc microClassDoc, String tableName, List<Field> fields) {
        StringBuilder indexSb = new StringBuilder();
        StringBuilder sb = new StringBuilder();
        sb.append(String.format(MicroDoclet.ColumnType.TABLE_SQL_PREFIX, tableName, tableName));
        for (Field field : fields) {
            // 获取列类型
            String javaType = field.getType().getName();
            // 获取列名
            String fieldName = field.getName();
            TableField tableField = field.getAnnotation(TableField.class);
            if (tableField != null) {
                if (StringUtils.isNotBlank(tableField.value())) {
                    fieldName = tableField.value();
                }
            }
            TableId tableId = field.getAnnotation(TableId.class);
            if (tableId != null) {
                if (StringUtils.isNotBlank(tableId.value())) {
                    fieldName = tableId.value();
                }
                indexSb.append(String.format(MicroDoclet.ColumnType.PRIMARY_KEY, fieldName));
            }
            String columnName = humpToUnderline(fieldName);

            // 获取列父类型
            String sqlType;
            String superColumnName = field.getType().getSuperclass().getName();
            if (MicroDoclet.ColumnType.JAVA_ENUM.equals(superColumnName)) {
                sqlType = MicroDoclet.ColumnType.parse(MicroDoclet.ColumnType.JAVA_ENUM);
            } else if (tableId != null) {
                sqlType = MicroDoclet.ColumnType.parse(javaType) + MicroDoclet.ColumnType.PRIMARY_KEY_SQL_COLUMN;
            } else {
                sqlType = MicroDoclet.ColumnType.parse(javaType);
            }

            MicroDoclet.MicroFieldDoc microFieldDoc = microClassDoc.getFieldMap().get(fieldName);
            if (microFieldDoc.isForeignKey()) {
                indexSb.append(",\n").append(String.format(MicroDoclet.ColumnType.INDEX_SQL, columnName, columnName));
            }
            if (StringUtils.isNotBlank(microFieldDoc.getSerial())) {
                String tempJavaType = javaType;
                if (MicroDoclet.ColumnType.JAVA_ENUM.equals(superColumnName)) {
                    tempJavaType = MicroDoclet.ColumnType.JAVA_ENUM;
                }
                sqlType = MicroDoclet.ColumnType.parse(microFieldDoc.getSerial(), tempJavaType);
            }
            if (StringUtils.isBlank(microFieldDoc.getSerialField())) {
                sb.append(String.format(MicroDoclet.ColumnType.TABLE_SQL_COLUMN,
                        columnName, sqlType, microFieldDoc.getComment())).append(MicroDoclet.ColumnType.NEWLINE);
            } else {
                sb.append(String.format(MicroDoclet.ColumnTemplate.parse(microFieldDoc.getSerialField()),
                        columnName, microFieldDoc.getComment())).append(MicroDoclet.ColumnType.NEWLINE);
            }
        }
        if (StringUtils.isBlank(indexSb.toString())) {
            throw new IllegalArgumentException("必须要设置主键字段");
        }

        sb.append(indexSb.toString());
        sb.append(String.format(MicroDoclet.ColumnType.TABLE_SQL_SUFFIX, microClassDoc.getComment()));
        return sb.toString();
    }

    private List<Field> recursionField(List<Field> fieldList, Class<?> clz) {
        for (Field field : clz.getDeclaredFields()) {
            if (Modifier.isTransient(field.getModifiers()) ||
                    Modifier.isStatic(field.getModifiers())) {
                continue;
            }

            fieldList.add(field);
        }

        Class<?> superClass = clz.getSuperclass();
        if (Object.class.getName().equals(superClass.getName())) {
            return fieldList;
        }

        return recursionField(fieldList, superClass);
    }

    private List<Field> sortField(List<Field> fieldList) {
        List<Field> fields = new ArrayList<>();
        for (Field field : fieldList) {
            if (MicroEntity.ID_FIELD.equals(field.getName())) {
                fields.add(field);
                break;
            }
        }
        for (Field field : fieldList) {
            if (MicroEntity.ID_FIELD.equals(field.getName())) {
                continue;
            }
            fields.add(field);
        }

        return fields;
    }

    /***
     * 驼峰命名转为下划线命名
     *
     * @param para
     *        驼峰命名的字符串
     */

    private String humpToUnderline(String para) {
        StringBuilder sb = new StringBuilder(para);
        int temp = 0;
        if (!para.contains("_")) {
            for (int i = 0; i < para.length(); i++) {
                if (Character.isUpperCase(para.charAt(i))) {
                    sb.insert(i + temp, "_");
                    temp += 1;
                }
            }
        }

        return sb.toString().toLowerCase();
    }

}
