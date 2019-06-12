package cn.micro.biz.commons.code;

import cn.micro.biz.commons.mybatis.MicroEntity;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.sun.javadoc.ClassDoc;
import com.sun.javadoc.FieldDoc;
import com.sun.javadoc.Tag;
import com.sun.source.doctree.DocTree;
import org.apache.commons.lang3.StringUtils;
import org.micro.util.ClassUtils;

import java.io.File;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.*;

public class CodeDemo {

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
        realPathParams.add(MicroDoclet.class.getName());
        realPathParams.addAll(realPathList);
        com.sun.tools.javadoc.Main.execute(realPathParams.toArray(new String[0]));
        ClassDoc[] classes = MicroDoclet.rootDoc.classes();

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

            MicroClassDoc microClassDoc = new MicroClassDoc();
            microClassDoc.setComment(classCommentText);
            for (FieldDoc fieldDoc : fieldDocList) {
                String classFieldName = fieldDoc.qualifiedName();
                String fieldName = fieldDoc.name();

                String fieldCommentText;
                if (classFieldName.startsWith(MicroEntity.class.getName())) {
                    FieldDoc tempFieldDoc = microEntityFieldDocMap.get(fieldName);
                    fieldName = tempFieldDoc.name();
                    fieldCommentText = getCommentText(tempFieldDoc.commentText());
                } else {
                    fieldCommentText = getCommentText(fieldDoc.commentText());
                }

                MicroFieldDoc microFieldDoc = new MicroFieldDoc();
                microFieldDoc.setFieldName(fieldName);
                microFieldDoc.setSerial(getTag(DocTree.Kind.SERIAL.tagName, fieldDoc));
                microFieldDoc.setSerialField(getTag(DocTree.Kind.SERIAL_FIELD.tagName, fieldDoc));
                microFieldDoc.setComment(fieldCommentText);
                microClassDoc.getFieldMap().put(fieldName, microFieldDoc);
            }

            docMap.put(className, microClassDoc);
        }

        return docMap;
    }

    private static String getTag(String tag, FieldDoc fieldDoc) {
        Tag[] tags = fieldDoc.tags(tag);
        if (tags == null || tags.length == 0) {
            return null;
        }

        String tagStr = tags[0].text();
        int index = tagStr.indexOf(ColumnType.NEWLINE);
        if (index > 0) {
            tagStr = tagStr.substring(0, index);
        }
        return tagStr;
    }

    private static String getCommentText(String commentText) {
        int index = commentText.indexOf(ColumnType.NEWLINE);
        if (index > 0) {
            return commentText.substring(0, index);
        }

        return commentText;
    }

    private static List<FieldDoc> recursionFieldDoc(List<FieldDoc> fieldDocList, ClassDoc classDoc) {
        fieldDocList.addAll(Arrays.asList(classDoc.serializableFields()));
        ClassDoc superClassDoc = classDoc.superclass();
        if (Object.class.getName().equals(superClassDoc.qualifiedTypeName())) {
            return fieldDocList;
        }

        return recursionFieldDoc(fieldDocList, superClassDoc);
    }

    private static String buildSql(MicroClassDoc microClassDoc, String tableName, List<Field> fields) {
        StringBuilder pkSb = new StringBuilder();
        StringBuilder sb = new StringBuilder();
        sb.append(String.format(ColumnType.TABLE_SQL_PREFIX, tableName, tableName));
        for (Field field : fields) {
            // 获取列类型
            String javaType = field.getType().getName();
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
                if (StringUtils.isNotBlank(tableId.value())) {
                    columnName = tableId.value();
                }
                pkSb.append(String.format(ColumnType.PRIMARY_KEY, columnName));
            }

            // 获取列父类型
            String sqlType;
            String superColumnName = field.getType().getSuperclass().getName();
            if (ColumnType.JAVA_ENUM.equals(superColumnName)) {
                sqlType = ColumnType.parse(ColumnType.JAVA_ENUM);
            } else if (tableId != null) {
                sqlType = ColumnType.parse(javaType) + ColumnType.PRIMARY_KEY_SQL_COLUMN;
            } else {
                sqlType = ColumnType.parse(javaType);
            }

            MicroFieldDoc microFieldDoc = microClassDoc.getFieldMap().get(columnName);
            if (StringUtils.isNotBlank(microFieldDoc.getSerial())) {
                sqlType = ColumnType.parse(microFieldDoc.getSerial(), javaType);
            }
            if (StringUtils.isBlank(microFieldDoc.getSerialField())) {
                sb.append(String.format(ColumnType.TABLE_SQL_COLUMN,
                        columnName, sqlType, microFieldDoc.getComment())).append(ColumnType.NEWLINE);
            } else {
                sb.append(String.format(MicroColumnType.parse(microFieldDoc.getSerialField()),
                        columnName, microFieldDoc.getComment())).append(ColumnType.NEWLINE);
            }
        }
        if (StringUtils.isBlank(pkSb.toString())) {
            throw new IllegalArgumentException("必须要设置主键字段");
        }

        sb.append(pkSb.toString());
        sb.append(String.format(ColumnType.TABLE_SQL_SUFFIX, microClassDoc.getComment()));
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
        if (Object.class.getName().equals(superClass.getName())) {
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
