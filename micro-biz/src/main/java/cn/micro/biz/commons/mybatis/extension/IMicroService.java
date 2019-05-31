package cn.micro.biz.commons.mybatis.extension;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.support.SFunction;
import com.baomidou.mybatisplus.extension.service.IService;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;

/**
 * Micro Service
 * <p>
 *
 * @param <T>
 * @author lry
 */
public interface IMicroService<T> extends IService<T> {

    // ====== lambda equals(eq) to one object

    /**
     * The get one object by 1 equals(eq) bean field
     *
     * @param eqColumn eg: User::getName
     * @param eqValue  eg: "Tom"
     * @return {@link T }
     */
    default T getOne(SFunction<T, Serializable> eqColumn, Serializable eqValue) {
        return getOne(eqColumn, eqValue, null, null);
    }

    /**
     * The get one object by 2 equals(eq) bean field
     *
     * @param eqColumn1 eg: User::getName
     * @param eqValue1  eg: "Tom"
     * @param eqColumn2 eg: User::getCategory
     * @param eqValue2  eg: "STUDENT"
     * @return {@link T }
     */
    default T getOne(SFunction<T, Serializable> eqColumn1, Serializable eqValue1,
                     SFunction<T, Serializable> eqColumn2, Serializable eqValue2) {
        return this.getOne(buildEqQuery(eqColumn1, eqValue1, eqColumn2, eqValue2));
    }

    // ====== lambda equals(eq) to list object

    /**
     * The get list object by 1 equals(eq) bean field
     *
     * @param eqColumn eg: User::getName
     * @param eqValue  eg: "Tom"
     * @return {@link T }
     */
    default List<T> list(SFunction<T, Serializable> eqColumn, Serializable eqValue) {
        return list(eqColumn, eqValue, null, null);
    }

    /**
     * The get list object by 1 equals(eq) bean field
     *
     * @param eqColumn1 eg: User::getName
     * @param eqValue1  eg: "Tom"
     * @param eqColumn2 eg: User::getCategory
     * @param eqValue2  eg: "STUDENT"
     * @return {@link T }
     */
    default List<T> list(SFunction<T, Serializable> eqColumn1, Serializable eqValue1,
                         SFunction<T, Serializable> eqColumn2, Serializable eqValue2) {
        return this.list(buildEqQuery(eqColumn1, eqValue1, eqColumn2, eqValue2));
    }

    // ====== lambda equals(eq) to one object

    /**
     * The remove by 1 equals(eq) bean field
     *
     * @param eqColumn eg: User::getName
     * @param eqValue  eg: "Tom"
     * @return {@link T }
     */
    default boolean remove(SFunction<T, Serializable> eqColumn, Serializable eqValue) {
        return remove(eqColumn, eqValue, null, null);
    }

    /**
     * The remove by 2 equals(eq) bean field
     *
     * @param eqColumn1 eg: User::getName
     * @param eqValue1  eg: "Tom"
     * @param eqColumn2 eg: User::getCategory
     * @param eqValue2  eg: "STUDENT"
     * @return {@link T }
     */
    default boolean remove(
            SFunction<T, Serializable> eqColumn1, Serializable eqValue1,
            SFunction<T, Serializable> eqColumn2, Serializable eqValue2) {
        LambdaQueryWrapper<T> wrapper = new LambdaQueryWrapper<T>().eq(eqColumn1, eqValue1);
        if (eqColumn2 != null && eqValue2 != null) {
            wrapper.eq(eqColumn2, eqValue2);
        }

        return this.remove(wrapper);
    }

    // ====== lambda equals(eq) to one object

    /**
     * The update by 1 equals(eq) bean field
     *
     * @param entity   update entity
     * @param eqColumn eg: User::getName
     * @param eqValue  eg: "Tom"
     * @return {@link T }
     */
    default boolean update(T entity, SFunction<T, Serializable> eqColumn, Serializable eqValue) {
        return update(entity, eqColumn, eqValue, null, null);
    }

    /**
     * The update by 2 equals(eq) bean field
     *
     * @param entity    update entity
     * @param eqColumn1 eg: User::getName
     * @param eqValue1  eg: "Tom"
     * @param eqColumn2 eg: User::getCategory
     * @param eqValue2  eg: "STUDENT"
     * @return {@link T }
     */
    default boolean update(
            T entity,
            SFunction<T, Serializable> eqColumn1, Serializable eqValue1,
            SFunction<T, Serializable> eqColumn2, Serializable eqValue2) {
        LambdaQueryWrapper<T> wrapper = new LambdaQueryWrapper<T>().eq(eqColumn1, eqValue1);
        if (eqColumn2 != null && eqValue2 != null) {
            wrapper.eq(eqColumn2, eqValue2);
        }

        return this.update(entity, wrapper);
    }

    // ====== lambda in to list object

    /**
     * The get list by in bean field
     *
     * @param inColumn eg: User::getName
     * @param inValues eg: "Tom", "Jack"
     * @return {@link List<T> }
     */
    default List<T> listIn(SFunction<T, Serializable> inColumn, Object... inValues) {
        return this.list(new LambdaQueryWrapper<T>().in(inColumn, inValues));
    }

    /**
     * The get list by in bean field
     *
     * @param inColumn eg: User::getName
     * @param inValues eg: Arrays.asList("Tom", "Jack")
     * @return {@link List<T> }
     */
    default List<T> listIn(SFunction<T, Serializable> inColumn, Collection<Serializable> inValues) {
        return this.list(new LambdaQueryWrapper<T>().in(inColumn, inValues));
    }

    // ====== lambda equals(eq) and in to list object

    /**
     * The get list object by 1 equals(eq) and in bean field
     *
     * @param eqColumn eg: User::getName
     * @param eqValue  eg: "Tom"
     * @param inColumn eg: User::getArea
     * @param inValues eg: "beijing", "shanghai"
     * @return {@link List<T> }
     */
    default List<T> listEqAndIn(
            SFunction<T, Serializable> eqColumn, Serializable eqValue,
            SFunction<T, Serializable> inColumn, Object... inValues) {
        return listEqAndIn(eqColumn, eqValue, null, null, inColumn, inValues);
    }

    /**
     * The get list object by 2 equals(eq) and in bean field
     *
     * @param eqColumn1 eg: User::getName
     * @param eqValue1  eg: "Tom"
     * @param eqColumn2 eg: User::getCategory
     * @param eqValue2  eg: "STUDENT"
     * @param inColumn  eg: User::getArea
     * @param inValues  eg: "beijing", "shanghai"
     * @return {@link List<T> }
     */
    default List<T> listEqAndIn(
            SFunction<T, Serializable> eqColumn1, Serializable eqValue1,
            SFunction<T, Serializable> eqColumn2, Serializable eqValue2,
            SFunction<T, Serializable> inColumn, Object... inValues) {
        return this.list(buildEqQuery(eqColumn1, eqValue1, eqColumn2, eqValue2).in(inColumn, inValues));
    }

    /**
     * The get list object by 1 equals(eq) and in bean field
     *
     * @param eqColumn eg: User::getName
     * @param eqValue  eg: "Tom"
     * @param inColumn eg: User::getArea
     * @param inValues eg: Arrays.asList("beijing", "shanghai")
     * @return {@link List<T> }
     */
    default List<T> listEqAndIn(
            SFunction<T, Serializable> eqColumn, Serializable eqValue,
            SFunction<T, Serializable> inColumn, Collection<Serializable> inValues) {
        return listEqAndIn(eqColumn, eqValue, null, null, inColumn, inValues);
    }

    /**
     * The get list object by 2 equals(eq) and in bean field
     *
     * @param eqColumn1 eg: User::getName
     * @param eqValue1  eg: "Tom"
     * @param eqColumn2 eg: User::getCategory
     * @param eqValue2  eg: "STUDENT"
     * @param inColumn  eg: User::getArea
     * @param inValues  eg: Arrays.asList("beijing", "shanghai")
     * @return {@link List<T> }
     */
    default List<T> listEqAndIn(
            SFunction<T, Serializable> eqColumn1, Serializable eqValue1,
            SFunction<T, Serializable> eqColumn2, Serializable eqValue2,
            SFunction<T, Serializable> inColumn, Collection<Serializable> inValues) {
        return this.list(buildEqQuery(eqColumn1, eqValue1, eqColumn2, eqValue2).in(inColumn, inValues));
    }

    /**
     * Build eq in
     *
     * @param column1 eg: User::getName
     * @param value1  eg: "Tom"
     * @param column2 eg: User::getCategory
     * @param value2  eg: "STUDENT"
     * @return {@link LambdaQueryWrapper<T>}
     */
    default LambdaQueryWrapper<T> buildEqQuery(
            SFunction<T, Serializable> column1, Serializable value1,
            SFunction<T, Serializable> column2, Serializable value2) {
        LambdaQueryWrapper<T> wrapper = new LambdaQueryWrapper<T>().eq(column1, value1);
        if (column2 == null || value2 == null) {
            return wrapper;
        }

        return wrapper.eq(column2, value2);
    }

}
