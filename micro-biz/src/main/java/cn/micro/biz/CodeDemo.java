package cn.micro.biz;

import cn.micro.biz.commons.mybatis.MicroEntity;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.sun.javadoc.ClassDoc;
import com.sun.javadoc.FieldDoc;
import com.sun.javadoc.RootDoc;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.ToString;
import org.apache.commons.lang3.StringUtils;
import org.micro.util.ClassUtils;

import java.io.File;
import java.io.Serializable;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.*;

public class CodeDemo {

    private static final String TABLE_SQL_PREFIX = "CREATE TABLE `%s` (\n";
    private static final String TABLE_SQL_SUFFIX = "\n) ENGINE = InnoDB AUTO_INCREMENT = 2 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '%s' ROW_FORMAT = Dynamic;";

    @Getter
    @AllArgsConstructor
    enum ColumnDataType {

        // ===

        TABLE_ID("TableId", "java.lang.Long", "PRIMARY KEY (`%s`) USING BTREE"),
        STRING("", "java.lang.String", "`%s` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '%s'"),
        PK_LONG("PK", "java.lang.Long", "`%s` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '%s'"),
        LONG("", "java.lang.Long", "`%s` bigint(20) COMMENT '%s'"),
        INTEGER("", "java.lang.Integer", "`%s` tinyint(1) UNSIGNED ZEROFILL NULL DEFAULT 0 COMMENT '%s'"),
        ENUM("", "java.lang.Enum", "`%s` tinyint(1) UNSIGNED ZEROFILL NULL DEFAULT 0 COMMENT '%s'"),
        TIMESTAMP("", "java.sql.Timestamp", "`%s` timestamp(0) NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '%s'");

        private String key;
        private String javaType;
        private String sql;

        public static String parse(String key, String javaType) {
            for (ColumnDataType e : values()) {
                if (e.getJavaType().equals(javaType)) {
                    if (key == null || key.equals(e.getKey())) {
                        return e.getSql();
                    }
                }
            }

            throw new IllegalArgumentException("未知类型");
        }
    }

    public static void main(String[] args) {
        String realPath = CodeDemo.class.getResource("/").getPath();
        realPath = realPath.substring(0, realPath.length() - 15) + "src/main/java/";

        String packageName = "cn.micro.biz";
        Set<Class<?>> classSet = ClassUtils.getClasses(packageName);

        // 读取注释文档
        Map<String, MicroClassDoc> docMap = getDocument(realPath, classSet);

        for (Class<?> clz : classSet) {
            TableName tableNameAnnotation = clz.getDeclaredAnnotation(TableName.class);
            if (tableNameAnnotation != null) {
                String tableName = tableNameAnnotation.value();
                if (!"login_log".equals(tableName)) {
                    continue;
                }
                if (StringUtils.isBlank(tableName)) {
                    tableName = clz.getName();
                }

                MicroClassDoc microClassDoc = docMap.get(clz.getName());
                List<Field> fields = sortField(recursionField(new ArrayList<>(), clz));
                String sql = buildSql(microClassDoc, tableName, fields);
                System.out.println(sql);
                break;
            }
        }
    }

    private static String getPrimaryKey(List<Field> fields) {
        for (Field field : fields) {
            TableId tableId = field.getAnnotation(TableId.class);
            if (tableId != null) {

            }
        }

        return null;
    }

    private static Map<String, MicroClassDoc> getDocument(String realPath, Set<Class<?>> classSet) {
        // 读取
        List<String> realPathList = new ArrayList<>();
        realPathList.add(realPath + MicroEntity.class.getName().replace(".", File.separator) + ".java");
        for (Class<?> clz : classSet) {
            TableName tableNameAnnotation = clz.getDeclaredAnnotation(TableName.class);
            if (tableNameAnnotation != null) {
                if (!"login_log".equals(tableNameAnnotation.value())) {
                    continue;
                }

                String path = realPath + clz.getName().replace(".", File.separator) + ".java";
                System.out.println(path);
                realPathList.add(path);
                break;
            }
        }
        return getDoc(realPathList);
    }

    private static Map<String, MicroClassDoc> getDoc(List<String> realPathList) {
        Map<String, MicroClassDoc> docMap = new HashMap<>();
        List<String> realPathParams = new ArrayList<>();
        realPathParams.add("-doclet");
        realPathParams.add(Doclet.class.getName());
        realPathParams.addAll(realPathList);
        com.sun.tools.javadoc.Main.execute(realPathParams.toArray(new String[0]));
        ClassDoc[] classes = Doclet.rootDoc.classes();

        // 获取通用父类
        Map<String, FieldDoc> microEntityFieldDocMap = new HashMap<>();
        for (ClassDoc classDoc : classes) {
            String className = classDoc.qualifiedTypeName();
            if (className.equals(MicroEntity.class.getName())) {
                for (FieldDoc fieldDoc : classDoc.serializableFields()) {
                    microEntityFieldDocMap.put(fieldDoc.name(), fieldDoc);
                }
                break;
            }
        }

        for (ClassDoc classDoc : classes) {
            String className = classDoc.qualifiedTypeName();
            String classCommentText = getCommentText(classDoc.commentText());

            List<FieldDoc> fieldDocList = new ArrayList<>();
            recursionFieldDoc(fieldDocList, classDoc);
            Map<String, String> fieldDocMap = new HashMap<>();
            for (FieldDoc fieldDoc : fieldDocList) {
                String classFieldName = fieldDoc.qualifiedName();
                String fieldName = fieldDoc.name();
                if (classFieldName.startsWith(MicroEntity.class.getName())) {
                    FieldDoc tempFieldDoc = microEntityFieldDocMap.get(fieldName);
                    fieldName = tempFieldDoc.name();
                    String fieldCommentText = getCommentText(tempFieldDoc.commentText());
                    fieldDocMap.put(fieldName, fieldCommentText);
                } else {

                    String fieldCommentText = getCommentText(fieldDoc.commentText());
                    fieldDocMap.put(fieldName, fieldCommentText);
                }
            }
            MicroClassDoc microClassDoc = new MicroClassDoc();
            microClassDoc.setComment(classCommentText);
            microClassDoc.setFieldMap(fieldDocMap);
            docMap.put(className, microClassDoc);
        }

        return docMap;
    }

    private static String getCommentText(String commentText) {
        if (commentText.indexOf("\n") > 0) {
            return commentText.substring(0, commentText.indexOf("\n"));
        }

        return commentText;
    }

    @Data
    @ToString
    private static class MicroClassDoc implements Serializable {
        private String comment;
        private Map<String, String> fieldMap;
    }

    private static List<FieldDoc> recursionFieldDoc(List<FieldDoc> fieldDocList, ClassDoc classDoc) {
        fieldDocList.addAll(Arrays.asList(classDoc.serializableFields()));
        ClassDoc superClassDoc = classDoc.superclass();
        if ("java.lang.Object".equals(superClassDoc.qualifiedTypeName())) {
            return fieldDocList;
        }

        return recursionFieldDoc(fieldDocList, superClassDoc);
    }

    public static class Doclet {

        private static RootDoc rootDoc;

        public static boolean start(RootDoc rootDoc) {
            Doclet.rootDoc = rootDoc;
            return true;
        }
    }

    private static String buildSql(MicroClassDoc microClassDoc, String tableName, List<Field> fields) {
        StringBuilder pkSb = new StringBuilder();
        StringBuilder sb = new StringBuilder();
        sb.append(String.format(TABLE_SQL_PREFIX, tableName));
        for (Field field : fields) {
            String pk = null;
            // 获取列类型
            String columnType = field.getType().getName();
            // 获取列名
            String columnName = field.getName();
            TableField tableField = field.getAnnotation(TableField.class);
            if (tableField != null) {
                if (StringUtils.isNotBlank(tableField.value())) {
                    columnName = tableField.value();
                }
            }
            TableId tableId = field.getAnnotation(TableId.class);
            if (tableId != null) {
                pk = "PK";
                if (StringUtils.isNotBlank(tableId.value())) {
                    columnName = tableId.value();
                }
                String sqlTemplate = ColumnDataType.parse(TableId.class.getSimpleName(), columnType);
                pkSb.append(String.format(sqlTemplate, columnName));
            }

            // 获取列父类型
            String columnSql;
            String superColumnName = field.getType().getSuperclass().getName();
            if ("java.lang.Enum".equals(superColumnName)) {
                columnSql = ColumnDataType.parse(null, superColumnName);
            } else {
                columnSql = ColumnDataType.parse(pk, columnType);
            }

            sb.append(String.format(columnSql, columnName, microClassDoc.getFieldMap().get(columnName)));
            sb.append(",\n");
        }
        if (StringUtils.isBlank(pkSb.toString())) {
            throw new IllegalArgumentException("必须要设置主键字段");
        }

        sb.append(pkSb.toString());
        sb.append(String.format(TABLE_SQL_SUFFIX, microClassDoc.getComment()));
        return sb.toString();
    }

    private static List<Field> recursionField(List<Field> fieldList, Class<?> clz) {
        for (Field field : clz.getDeclaredFields()) {
            if (Modifier.isTransient(field.getModifiers()) ||
                    Modifier.isStatic(field.getModifiers())) {
                continue;
            }

            fieldList.add(field);
        }

        Class<?> superClass = clz.getSuperclass();
        if ("java.lang.Object".equals(superClass.getName())) {
            return fieldList;
        }

        return recursionField(fieldList, superClass);
    }

    private static List<Field> sortField(List<Field> fieldList) {
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

}
