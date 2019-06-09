package cn.micro.biz.commons.enums;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import java.io.Serializable;

/**
 * IEnum Interface
 *
 * @author lry
 */
@JsonDeserialize(using = IEnumJsonDeserializer.class)
public interface IEnum<T extends Serializable> {

    String VALUE_KEY = "value";
    String TITLE_KEY = "title";

    /**
     * The enum name
     *
     * @return name
     */
    String name();

    /**
     * The enum value
     *
     * @return value
     */
    T getValue();

    /**
     * The enum title
     *
     * @return title
     */
    String getTitle();

    /**
     * The parse value
     *
     * @param clz   {@link Class<E>}
     * @param value enum {@link IEnum#name()} or {@link IEnum#getValue()}
     * @param <E>   {@link Class<E>}
     * @return {@link E}
     */
    static <E extends IEnum> E parse(Class<E> clz, Object value) {
        for (E e : clz.getEnumConstants()) {
            if (e.getValue() == value || e.getValue().equals(value) || e.name().equals(value)) {
                return e;
            }
        }

        throw new IllegalArgumentException("Illegal Argument");
    }

}

