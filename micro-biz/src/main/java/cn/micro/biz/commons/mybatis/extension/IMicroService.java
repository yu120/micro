package cn.micro.biz.commons.mybatis.extension;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.support.SFunction;
import com.baomidou.mybatisplus.extension.service.IService;

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
     * @param column eg: User::getName
     * @param value  eg: "Tom"
     * @return {@link T }
     */
    default T getOneEqs(SFunction<T, ?> column, Object value) {
        return getOneEqs(column, value, null, null);
    }

    /**
     * The get one object by 2 equals(eq) bean field
     *
     * @param column1 eg: User::getName
     * @param value1  eg: "Tom"
     * @param column2 eg: User::getCategory
     * @param value2  eg: "STUDENT"
     * @return {@link T }
     */
    default T getOneEqs(SFunction<T, ?> column1, Object value1, SFunction<T, ?> column2, Object value2) {
        return getOne(buildEqQuery(column1, value1, column2, value2));
    }

    // ====== lambda equals(eq) to list object

    /**
     * The get list object by 1 equals(eq) bean field
     *
     * @param column eg: User::getName
     * @param value  eg: "Tom"
     * @return {@link T }
     */
    default List<T> listEqs(SFunction<T, ?> column, Object value) {
        return listEqs(column, value, null, null);
    }

    /**
     * The get list object by 1 equals(eq) bean field
     *
     * @param column1 eg: User::getName
     * @param value1  eg: "Tom"
     * @param column2 eg: User::getCategory
     * @param value2  eg: "STUDENT"
     * @return {@link T }
     */
    default List<T> listEqs(SFunction<T, ?> column1, Object value1, SFunction<T, ?> column2, Object value2) {
        return list(buildEqQuery(column1, value1, column2, value2));
    }

    // ====== lambda in to list object

    /**
     * The get list by in bean field
     *
     * @param column eg: User::getName
     * @param values eg: "Tom", "Jack"
     * @return {@link List<T> }
     */
    default List<T> listIns(SFunction<T, ?> column, Object... values) {
        return list(new LambdaQueryWrapper<T>().in(column, values));
    }

    /**
     * The get list by in bean field
     *
     * @param column eg: User::getName
     * @param values eg: Arrays.asList("Tom", "Jack")
     * @return {@link List<T> }
     */
    default List<T> listIns(SFunction<T, ?> column, Collection<?> values) {
        return list(new LambdaQueryWrapper<T>().in(column, values));
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
            SFunction<T, ?> eqColumn, Object eqValue,
            SFunction<T, ?> inColumn, Object... inValues) {
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
            SFunction<T, ?> eqColumn1, Object eqValue1,
            SFunction<T, ?> eqColumn2, Object eqValue2,
            SFunction<T, ?> inColumn, Object... inValues) {
        return list(buildEqQuery(eqColumn1, eqValue1, eqColumn2, eqValue2).in(inColumn, inValues));
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
            SFunction<T, ?> eqColumn, Object eqValue,
            SFunction<T, ?> inColumn, Collection<?> inValues) {
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
            SFunction<T, ?> eqColumn1, Object eqValue1,
            SFunction<T, ?> eqColumn2, Object eqValue2,
            SFunction<T, ?> inColumn, Collection<?> inValues) {
        return list(buildEqQuery(eqColumn1, eqValue1, eqColumn2, eqValue2).in(inColumn, inValues));
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
    default LambdaQueryWrapper<T> buildEqQuery(SFunction<T, ?> column1, Object value1,
                                               SFunction<T, ?> column2, Object value2) {
        LambdaQueryWrapper<T> wrapper = new LambdaQueryWrapper<T>().eq(column1, value1);
        if (column2 != null && value2 != null) {
            wrapper.eq(column2, value2);
        }

        return wrapper;
    }

}
