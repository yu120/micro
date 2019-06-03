package cn.micro.biz.commons.mybatis.extension;

import cn.micro.biz.commons.exception.MicroErrorException;
import cn.micro.biz.commons.mybatis.extension.enums.EnumValue;
import cn.micro.biz.commons.mybatis.extension.enums.IEnum;
import org.apache.ibatis.logging.Log;
import org.apache.ibatis.logging.LogFactory;
import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 自定义枚举属性转换器
 *
 * @author lry
 */
public class EnumTypeHandler<E extends Enum<?>> extends BaseTypeHandler<Enum<?>> {

    private static final Log LOGGER = LogFactory.getLog(EnumTypeHandler.class);

    private static final Map<Class<?>, Method> TABLE_METHOD_OF_ENUM_TYPES = new ConcurrentHashMap<>();

    private final Class<E> type;

    private final Method method;
    private final String returnType;

    public EnumTypeHandler(Class<E> type) {
        if (type == null) {
            throw new IllegalArgumentException("Type argument cannot be null");
        }
        this.type = type;
        if (IEnum.class.isAssignableFrom(type)) {
            try {
                this.method = type.getMethod("getValue");
            } catch (NoSuchMethodException e) {
                throw new IllegalArgumentException(String.format("NoSuchMethod getValue() in Class: %s.", type.getName()));
            }
        } else {
            this.method = TABLE_METHOD_OF_ENUM_TYPES.computeIfAbsent(type, k -> {
                Field field = dealEnumType(this.type).orElseThrow(() ->
                        new IllegalArgumentException(String.format("Could not find @EnumValue in Class: %s.", type.getName())));
                return getMethod(this.type, field);
            });
        }

        this.returnType = method.getReturnType().getName();
    }

    @SuppressWarnings("Duplicates")
    @Override
    public void setNonNullParameter(PreparedStatement ps, int i, Enum<?> parameter, JdbcType jdbcType)
            throws SQLException {
        try {
            this.method.setAccessible(true);
            if (jdbcType == null) {
                ps.setObject(i, this.method.invoke(parameter));
            } else {
                // see r3589
                ps.setObject(i, this.method.invoke(parameter), jdbcType.TYPE_CODE);
            }
        } catch (IllegalAccessException e) {
            LOGGER.error("unrecognized jdbcType, failed to set StringValue for type=" + parameter);
        } catch (InvocationTargetException e) {
            throw new MicroErrorException("Error: NoSuchMethod in " + this.type.getName() + ".  Cause:" + e.getMessage(), e);
        }
    }

    @Override
    public E getNullableResult(ResultSet rs, String columnName) throws SQLException {
        if (null == getResultSet(rs, columnName) && rs.wasNull()) {
            return null;
        }
        return valueOf(this.type, getResultSet(rs, columnName), this.method);
    }

    @Override
    public E getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
        if (null == getResultSet(rs, columnIndex) && rs.wasNull()) {
            return null;
        }
        return valueOf(this.type, getResultSet(rs, columnIndex), this.method);
    }

    @Override
    public E getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
        if (null == cs.getObject(columnIndex) && cs.wasNull()) {
            return null;
        }
        return valueOf(this.type, cs.getObject(columnIndex), this.method);
    }

    private static Optional<Field> dealEnumType(Class<?> clazz) {
        return clazz.isEnum() ? Arrays.stream(clazz.getDeclaredFields()).filter(
                field -> field.isAnnotationPresent(EnumValue.class)).findFirst() : Optional.empty();
    }

    private Object getResultSet(ResultSet rs, Object column) throws SQLException {
        if ("int".equals(returnType) || "java.lang.Integer".equals(returnType)) {
            if (column instanceof String) {
                return rs.getInt(String.valueOf(column));
            } else {
                return rs.getInt((int) column);
            }
        } else if ("double".equals(returnType) || "java.lang.Double".equals(returnType)) {
            if (column instanceof String) {
                return rs.getDouble(String.valueOf(column));
            } else {
                return rs.getDouble((int) column);
            }
        } else if ("long".equals(returnType) || "java.lang.Long".equals(returnType)) {
            if (column instanceof String) {
                return rs.getLong(String.valueOf(column));
            } else {
                return rs.getLong((int) column);
            }
        } else if ("float".equals(returnType) || "java.lang.Float".equals(returnType)) {
            if (column instanceof String) {
                return rs.getFloat(String.valueOf(column));
            } else {
                return rs.getFloat((int) column);
            }
        } else if ("java.lang.String".equals(returnType)) {
            if (column instanceof String) {
                return rs.getString(String.valueOf(column));
            } else {
                return rs.getString((int) column);
            }
        } else {
            if (column instanceof String) {
                return rs.getObject(String.valueOf(column));
            } else {
                return rs.getObject((int) column);
            }
        }
    }

    /**
     * 获取字段get方法
     *
     * @param cls   class
     * @param field 字段
     * @return Get方法
     */
    private static Method getMethod(Class<?> cls, Field field) {
        try {
            return cls.getDeclaredMethod(getMethodCapitalize(field, field.getName()));
        } catch (NoSuchMethodException e) {
            throw new MicroErrorException("Error: NoSuchMethod in " + cls.getName() + ".  Cause:" + e.getMessage(), e);
        }
    }

    /**
     * <p>
     * 反射 method 方法名，例如 getId
     * </p>
     *
     * @param field {@link Field}
     * @param str   属性字符串内容
     */
    private static String getMethodCapitalize(Field field, final String str) {
        Class<?> fieldType = field.getType();
        return concatCapitalize(boolean.class.equals(fieldType) ? "is" : "get", str);
    }

    /**
     * 拼接字符串第二个字符串第一个字母大写
     */
    private static String concatCapitalize(String concatStr, final String str) {
        if (isEmpty(concatStr)) {
            concatStr = "";
        }
        if (str == null || str.length() == 0) {
            return str;
        }

        final char firstChar = str.charAt(0);
        if (Character.isTitleCase(firstChar)) {
            // already capitalized
            return str;
        }

        return concatStr + Character.toTitleCase(firstChar) + str.substring(1);
    }

    /**
     * 判断字符串是否为空
     *
     * @param cs 需要判断字符串
     * @return 判断结果
     */
    private static boolean isEmpty(final CharSequence cs) {
        int strLen;
        if (cs == null || (strLen = cs.length()) == 0) {
            return true;
        }
        for (int i = 0; i < strLen; i++) {
            if (!Character.isWhitespace(cs.charAt(i))) {
                return false;
            }
        }
        return true;
    }

    /**
     * 值映射为枚举
     *
     * @param enumClass 枚举类
     * @param value     枚举值
     * @param method    取值方法
     * @param <E>       对应枚举
     */
    private static <E extends Enum<?>> E valueOf(Class<E> enumClass, Object value, Method method) {
        E[] es = enumClass.getEnumConstants();
        for (E e : es) {
            Object eValue;
            try {
                method.setAccessible(true);
                eValue = method.invoke(e);
            } catch (IllegalAccessException | InvocationTargetException e1) {
                throw new MicroErrorException("Error: NoSuchMethod in " + enumClass.getName() + ".  Cause:" + e1.getMessage(), e1);
            }
            if (value instanceof Number && eValue instanceof Number
                    && new BigDecimal(String.valueOf(value)).compareTo(new BigDecimal(String.valueOf(eValue))) == 0) {
                return e;
            }
            if (Objects.equals(eValue, value)) {
                return e;
            }
        }

        return null;
    }

}
