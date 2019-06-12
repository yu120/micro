package cn.micro.biz.commons.code;

import cn.micro.biz.commons.mybatis.MicroEntity;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.sun.javadoc.ClassDoc;
import com.sun.javadoc.FieldDoc;
import com.sun.javadoc.SeeTag;
import com.sun.javadoc.Tag;
import com.sun.source.doctree.DocTree;
import org.apache.commons.lang3.StringUtils;
import org.micro.util.ClassUtils;

import java.io.File;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.*;

public enum CodeFactory {

    // ====

    INSTANCE;

    public List<String> handler(String realPath, String packageName) {
        List<String> sqlList = new ArrayList<>();
        // 读取Class列表
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
                tableName = humpToUnderline(tableName);

                MicroClassDoc microClassDoc = docMap.get(clz.getName());
                List<Field> fields = sortField(recursionField(new ArrayList<>(), clz));
                String sql = buildSql(microClassDoc, tableName, fields);
                sqlList.add(sql);
                break;
            }
        }

        return sqlList;
    }

    private Map<String, MicroClassDoc> getDocument(String realPath, Set<Class<?>> classSet) {
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

    private Map<String, MicroClassDoc> getDoc(List<String> realPathList) {
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
            microClassDoc.setClassName(className);
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
                SeeTag seeTag = getSeeTag(fieldDoc);
                if (seeTag != null) {
                    microFieldDoc.setSee(seeTag.text());
                    microFieldDoc.setSeeWhere(seeTag.referencedClassName());
                    microFieldDoc.setSeeWhat(seeTag.referencedMemberName());
                    try {
                        Class<?> clz = Class.forName(microFieldDoc.getSeeWhere());
                        Field field = null;
                        try {
                            field = clz.getDeclaredField(microFieldDoc.getSeeWhat());
                        } catch (Exception ignore) {
                        }
                        if (field == null) {
                            field = clz.getSuperclass().getDeclaredField(microFieldDoc.getSeeWhat());
                        }
                        TableId tableIdAnnotation = field.getDeclaredAnnotation(TableId.class);
                        microFieldDoc.setForeignKey(tableIdAnnotation != null);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                microClassDoc.getFieldMap().put(fieldName, microFieldDoc);
            }

            docMap.put(className, microClassDoc);
        }

        return docMap;
    }

    private SeeTag getSeeTag(FieldDoc fieldDoc) {
        SeeTag[] seeTags = fieldDoc.seeTags();
        if (seeTags == null || seeTags.length == 0) {
            return null;
        }

        return seeTags[0];
    }

    private String getTag(String tag, FieldDoc fieldDoc) {
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

    private String getCommentText(String commentText) {
        int index = commentText.indexOf(ColumnType.NEWLINE);
        if (index > 0) {
            return commentText.substring(0, index);
        }

        return commentText;
    }

    private List<FieldDoc> recursionFieldDoc(List<FieldDoc> fieldDocList, ClassDoc classDoc) {
        fieldDocList.addAll(Arrays.asList(classDoc.serializableFields()));
        ClassDoc superClassDoc = classDoc.superclass();
        if (Object.class.getName().equals(superClassDoc.qualifiedTypeName())) {
            return fieldDocList;
        }

        return recursionFieldDoc(fieldDocList, superClassDoc);
    }

    private String buildSql(MicroClassDoc microClassDoc, String tableName, List<Field> fields) {
        StringBuilder indexSb = new StringBuilder();
        StringBuilder sb = new StringBuilder();
        sb.append(String.format(ColumnType.TABLE_SQL_PREFIX, tableName, tableName));
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
                indexSb.append(String.format(ColumnType.PRIMARY_KEY, fieldName));
            }
            String columnName = humpToUnderline(fieldName);

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

            MicroFieldDoc microFieldDoc = microClassDoc.getFieldMap().get(fieldName);
            if (microFieldDoc.isForeignKey()) {
                indexSb.append(",\n").append(String.format(ColumnType.INDEX_SQL, columnName, columnName));
            }
            if (StringUtils.isNotBlank(microFieldDoc.getSerial())) {
                sqlType = ColumnType.parse(microFieldDoc.getSerial(), javaType);
            }
            if (StringUtils.isBlank(microFieldDoc.getSerialField())) {
                sb.append(String.format(ColumnType.TABLE_SQL_COLUMN,
                        columnName, sqlType, microFieldDoc.getComment())).append(ColumnType.NEWLINE);
            } else {
                sb.append(String.format(ColumnTemplate.parse(microFieldDoc.getSerialField()),
                        columnName, microFieldDoc.getComment())).append(ColumnType.NEWLINE);
            }
        }
        if (StringUtils.isBlank(indexSb.toString())) {
            throw new IllegalArgumentException("必须要设置主键字段");
        }

        sb.append(indexSb.toString());
        sb.append(String.format(ColumnType.TABLE_SQL_SUFFIX, microClassDoc.getComment()));
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
