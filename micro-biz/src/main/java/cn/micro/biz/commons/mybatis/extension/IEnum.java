package cn.micro.biz.commons.mybatis.extension;

import java.io.Serializable;

/**
 * 自定义枚举接口
 *
 * @author lry
 */
public interface IEnum<E, T extends Serializable> {

    /**
     * The parse value
     *
     * @param clz   {@link Class<E>}
     * @param value {@link Class<T>}
     * @param <E>   {@link Class<E>}
     * @param <T>   {@link Class<T>}
     * @return {@link E}
     */
    static <E extends IEnum, T> E parse(Class<E> clz, T value) {
        for (E e : clz.getEnumConstants()) {
            if (e.getValue() == value || e.getValue().equals(value)) {
                return e;
            }
        }

        throw new IllegalArgumentException("Illegal Argument");
    }

    T getValue();

}

