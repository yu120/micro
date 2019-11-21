package cn.micro.biz.commons.code;

import cn.micro.biz.commons.mybatis.MicroEntity;
import com.baomidou.mybatisplus.annotation.TableId;
//import com.sun.javadoc.*;
//import com.sun.source.doctree.DocTree;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.ToString;
import org.apache.commons.lang3.StringUtils;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.*;

/**
 * MicroDoclet
 *
 * @author lry
 */
public class MicroDoclet {
//
//    static RootDoc rootDoc;
//
//    public static boolean start(RootDoc rootDoc) {
//        MicroDoclet.rootDoc = rootDoc;
//        return true;
//    }
//
//    public static Map<String, MicroClassDoc> getDoc(List<String> realPathList) {
//        Map<String, MicroClassDoc> docMap = new HashMap<>();
//        List<String> realPathParams = new ArrayList<>();
//        realPathParams.add("-doclet");
//        realPathParams.add(MicroDoclet.class.getName());
//        realPathParams.addAll(realPathList);
//        com.sun.tools.javadoc.Main.execute(realPathParams.toArray(new String[0]));
//        ClassDoc[] classes = MicroDoclet.rootDoc.classes();
//
//        // 获取通用父类
//        Map<String, FieldDoc> microEntityFieldDocMap = new HashMap<>();
//        for (ClassDoc classDoc : classes) {
//            String className = classDoc.qualifiedTypeName();
//            if (className.equals(MicroEntity.class.getName())) {
//                for (FieldDoc fieldDoc : classDoc.serializableFields()) {
//                    microEntityFieldDocMap.put(fieldDoc.name(), fieldDoc);
//                }
//                break;
//            }
//        }
//
//        for (ClassDoc classDoc : classes) {
//            String className = classDoc.qualifiedTypeName();
//            String classCommentText = getCommentText(classDoc.commentText());
//            if (classCommentText.endsWith(" Entity")) {
//                classCommentText = classCommentText.substring(0, classCommentText.length() - 7);
//            }
//            classCommentText = classCommentText.replace("'", "");
//
//            List<FieldDoc> fieldDocList = new ArrayList<>();
//            recursionFieldDoc(fieldDocList, classDoc);
//
//            MicroClassDoc microClassDoc = new MicroClassDoc();
//            microClassDoc.setClassName(className);
//            microClassDoc.setComment(classCommentText);
//            for (FieldDoc fieldDoc : fieldDocList) {
//                String classFieldName = fieldDoc.qualifiedName();
//                String fieldName = fieldDoc.name();
//
//                String fieldCommentText;
//                if (classFieldName.startsWith(MicroEntity.class.getName())) {
//                    FieldDoc tempFieldDoc = microEntityFieldDocMap.get(fieldName);
//                    fieldName = tempFieldDoc.name();
//                    fieldCommentText = getCommentText(tempFieldDoc.commentText());
//                } else {
//                    fieldCommentText = getCommentText(fieldDoc.commentText());
//                }
//                fieldCommentText = fieldCommentText.replace("'", "");
//
//                MicroFieldDoc microFieldDoc = new MicroFieldDoc();
//                microFieldDoc.setFieldName(fieldName);
//                microFieldDoc.setSerial(getTag(DocTree.Kind.SERIAL.tagName, fieldDoc));
//                microFieldDoc.setSerialField(getTag(DocTree.Kind.SERIAL_FIELD.tagName, fieldDoc));
//                microFieldDoc.setComment(fieldCommentText);
//                SeeTag seeTag = getSeeTag(fieldDoc);
//                if (seeTag != null) {
//                    microFieldDoc.setSee(seeTag.text());
//                    microFieldDoc.setSeeWhere(seeTag.referencedClassName());
//                    microFieldDoc.setSeeWhat(seeTag.referencedMemberName());
//                    try {
//                        Class<?> clz = Class.forName(microFieldDoc.getSeeWhere());
//                        Field field = null;
//                        try {
//                            field = clz.getDeclaredField(microFieldDoc.getSeeWhat());
//                        } catch (Exception ignore) {
//                        }
//                        if (field == null) {
//                            field = clz.getSuperclass().getDeclaredField(microFieldDoc.getSeeWhat());
//                        }
//                        TableId tableIdAnnotation = field.getDeclaredAnnotation(TableId.class);
//                        microFieldDoc.setForeignKey(tableIdAnnotation != null);
//                    } catch (Exception e) {
//                        e.printStackTrace();
//                    }
//                }
//                microClassDoc.getFieldMap().put(fieldName, microFieldDoc);
//            }
//
//            docMap.put(className, microClassDoc);
//        }
//
//        return docMap;
//    }
//
//    private static SeeTag getSeeTag(FieldDoc fieldDoc) {
//        SeeTag[] seeTags = fieldDoc.seeTags();
//        if (seeTags == null || seeTags.length == 0) {
//            return null;
//        }
//
//        return seeTags[0];
//    }
//
//    private static String getTag(String tag, FieldDoc fieldDoc) {
//        Tag[] tags = fieldDoc.tags(tag);
//        if (tags == null || tags.length == 0) {
//            return null;
//        }
//
//        String tagStr = tags[0].text();
//        int index = tagStr.indexOf(ColumnType.NEWLINE);
//        if (index > 0) {
//            tagStr = tagStr.substring(0, index);
//        }
//        return tagStr;
//    }
//
//    private static String getCommentText(String commentText) {
//        int index = commentText.indexOf(ColumnType.NEWLINE);
//        if (index > 0) {
//            return commentText.substring(0, index);
//        }
//
//        return commentText;
//    }
//
//    private static List<FieldDoc> recursionFieldDoc(List<FieldDoc> fieldDocList, ClassDoc classDoc) {
//        fieldDocList.addAll(Arrays.asList(classDoc.serializableFields()));
//        ClassDoc superClassDoc = classDoc.superclass();
//        if (Object.class.getName().equals(superClassDoc.qualifiedTypeName())) {
//            return fieldDocList;
//        }
//
//        return recursionFieldDoc(fieldDocList, superClassDoc);
//    }

    @Data
    @ToString
    public static class MicroClassDoc implements Serializable {

        private String className;
        private String comment;
        private Map<String, MicroFieldDoc> fieldMap = new LinkedHashMap<>();

    }

    @Data
    @ToString
    public static class MicroFieldDoc implements Serializable {

        private String fieldName;
        private String comment;
        private String serial;
        private String serialField;

        private boolean foreignKey;
        private String see;
        private String seeWhere;
        private String seeWhat;

    }

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

    @Getter
    @AllArgsConstructor
    public enum ColumnType {

        // ===

        STRING("java.lang.String", "varchar(100)", "CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL"),
        LONG("java.lang.Long", "bigint(20)", null),
        DOUBLE("java.lang.Double", "double(10,2)", null),
        INTEGER("java.lang.Integer", "int(11)", null),
        ENUM("java.lang.Enum", "tinyint(1)", "UNSIGNED ZEROFILL"),
        TIMESTAMP("java.sql.Timestamp", "timestamp", "NULL"),
        BIG_DECIMAL("java.math.BigDecimal", "decimal(10, 2)", "NULL"),
        DATE("java.util.Date", "datetime(0)", "NULL"),
        SQL_DATE("java.sql.Date", "datetime(0)", "NULL"),
        ;

        private String javaType;
        private String sqlType;
        private String otherType;

        public static final String NEWLINE = "\n";
        public static final String JAVA_ENUM = "java.lang.Enum";
        public static final String PRIMARY_KEY_SQL_COLUMN = " NOT NULL AUTO_INCREMENT";
        public static final String PRIMARY_KEY = "PRIMARY KEY (`%s`) USING BTREE";
        public static final String INDEX_SQL = "INDEX `idx_%s`(`%s`) USING BTREE COMMENT 'Normal Index'";
        public static final String TABLE_SQL_PREFIX = "CREATE TABLE `%s` (\n";
        public static final String TABLE_SQL_COLUMN = "`%s` %s COMMENT '%s',";
        public static final String TABLE_SQL_SUFFIX = "\n) ENGINE = InnoDB AUTO_INCREMENT = 1 " +
                "CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '%s' ROW_FORMAT = Dynamic;";

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

}
