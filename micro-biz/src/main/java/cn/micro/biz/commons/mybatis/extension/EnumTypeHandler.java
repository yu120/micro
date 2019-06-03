package cn.micro.biz.commons.mybatis.extension;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.baomidou.mybatisplus.core.enums.IEnum;
import com.baomidou.mybatisplus.core.toolkit.EnumUtils;
import com.baomidou.mybatisplus.core.toolkit.ExceptionUtils;
import com.baomidou.mybatisplus.core.toolkit.ReflectionKit;
import org.apache.ibatis.logging.Log;
import org.apache.ibatis.logging.LogFactory;
import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.Map;
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
                return ReflectionKit.getMethod(this.type, field);
            });
        }

        this.returnType = method.getReturnType() + "";
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
            throw ExceptionUtils.mpe("Error: NoSuchMethod in %s.  Cause:", e, this.type.getName());
        }
    }

    @Override
    public E getNullableResult(ResultSet rs, String columnName) throws SQLException {
        if (null == getResultSet(rs, columnName) && rs.wasNull()) {
            return null;
        }
        return EnumUtils.valueOf(this.type, getResultSet(rs, columnName), this.method);
    }

    @Override
    public E getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
        if (null == getResultSet(rs, columnIndex) && rs.wasNull()) {
            return null;
        }
        return EnumUtils.valueOf(this.type, getResultSet(rs, columnIndex), this.method);
    }

    @Override
    public E getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
        if (null == cs.getObject(columnIndex) && cs.wasNull()) {
            return null;
        }
        return EnumUtils.valueOf(this.type, cs.getObject(columnIndex), this.method);
    }

    public static Optional<Field> dealEnumType(Class<?> clazz) {
        return clazz.isEnum() ? Arrays.stream(clazz.getDeclaredFields()).filter(
                field -> field.isAnnotationPresent(EnumValue.class)).findFirst() : Optional.empty();
    }

    private Object getResultSet(ResultSet rs, String columnName) throws SQLException {
        switch (returnType) {
            case "int":
                return rs.getInt(columnName);
            default:
                return rs.getObject(columnName);
        }
    }

    private Object getResultSet(ResultSet rs, int columnIndex) throws SQLException {
        switch (returnType) {
            case "int":
                return rs.getInt(columnIndex);
            default:
                return rs.getObject(columnIndex);
        }
    }

}
