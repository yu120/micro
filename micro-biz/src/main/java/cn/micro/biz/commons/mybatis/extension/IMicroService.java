package cn.micro.biz.commons.mybatis.extension;

import com.baomidou.mybatisplus.core.toolkit.support.SFunction;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.Collection;
import java.util.List;

/**
 * Micro Service
 * <p>
 * IN和EXISTS的区别:
 * 1.如果查询的两个表大小相当，那么用IN和EXISTS差别不大
 * 2.如果两个表中一个较小，一个是大表
 * 2.1 子查询表大的用EXISTS：SELECT * FROM a WHERE EXISTS (SELECT * FROM b WHERE b.id = a.id);
 * 2.2 子查询表小的用IN：SELECT * FROM a WHERE a.id IN (SELECT id FROM b);
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
    T getOneEqs(SFunction<T, ?> column, Object value);

    /**
     * The get one object by 2 equals(eq) bean field
     *
     * @param column1 eg: User::getName
     * @param value1  eg: "Tom"
     * @param column2 eg: User::getCategory
     * @param value2  eg: "STUDENT"
     * @return {@link T }
     */
    T getOneEqs(SFunction<T, ?> column1, Object value1,
                SFunction<T, ?> column2, Object value2);

    /**
     * The get one object by 3 equals(eq) bean field
     *
     * @param column1 eg: User::getName
     * @param value1  eg: "Tom"
     * @param column2 eg: User::getCategory
     * @param value2  eg: "STUDENT"
     * @param column3 eg: User::getArea
     * @param value3  eg: "beijing"
     * @return {@link T }
     */
    T getOneEqs(SFunction<T, ?> column1, Object value1,
                SFunction<T, ?> column2, Object value2,
                SFunction<T, ?> column3, Object value3);

    // ====== lambda equals(eq) to list object

    /**
     * The get list object by 1 equals(eq) bean field
     *
     * @param column eg: User::getName
     * @param value  eg: "Tom"
     * @return {@link T }
     */
    List<T> listEqs(SFunction<T, ?> column, Object value);

    /**
     * The get list object by 1 equals(eq) bean field
     *
     * @param column1 eg: User::getName
     * @param value1  eg: "Tom"
     * @param column2 eg: User::getCategory
     * @param value2  eg: "STUDENT"
     * @return {@link T }
     */
    List<T> listEqs(SFunction<T, ?> column1, Object value1,
                    SFunction<T, ?> column2, Object value2);

    /**
     * The get list object by 1 equals(eq) bean field
     *
     * @param column1 eg: User::getName
     * @param value1  eg: "Tom"
     * @param column2 eg: User::getCategory
     * @param value2  eg: "STUDENT"
     * @param column3 eg: User::getArea
     * @param value3  eg: "beijing"
     * @return {@link T }
     */
    List<T> listEqs(SFunction<T, ?> column1, Object value1,
                    SFunction<T, ?> column2, Object value2,
                    SFunction<T, ?> column3, Object value3);

    // ====== lambda in to list object

    /**
     * The get list by in bean field
     *
     * @param column eg: User::getName
     * @param values eg: "Tom", "Jack"
     * @return {@link List<T> }
     */
    List<T> listIns(SFunction<T, ?> column, Object... values);

    /**
     * The get list by in bean field
     *
     * @param column eg: User::getName
     * @param values eg: Arrays.asList("Tom", "Jack")
     * @return {@link List<T> }
     */
    List<T> listIns(SFunction<T, ?> column, Collection<?> values);

    // ====== lambda equals(eq) and in to list object

    /**
     * The get list object by 1 equals(eq) and in bean field
     *
     * @param eqColumn eg: User::getName
     * @param eqValue  eg: "Tom"
     * @param inColumn  eg: User::getArea
     * @param inValues  eg: "beijing", "shanghai"
     * @return {@link List<T> }
     */
    List<T> listEqAndIn(SFunction<T, ?> eqColumn, Object eqValue,
                        SFunction<T, ?> inColumn, Object... inValues);

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
    List<T> listEqAndIn(SFunction<T, ?> eqColumn1, Object eqValue1,
                        SFunction<T, ?> eqColumn2, Object eqValue2,
                        SFunction<T, ?> inColumn, Object... inValues);

    /**
     * The get list object by 1 equals(eq) and in bean field
     *
     * @param eqColumn eg: User::getName
     * @param eqValue  eg: "Tom"
     * @param inColumn  eg: User::getArea
     * @param inValues  eg: Arrays.asList("beijing", "shanghai")
     * @return {@link List<T> }
     */
    List<T> listEqAndIn(SFunction<T, ?> eqColumn, Object eqValue,
                        SFunction<T, ?> inColumn, Collection<?> inValues);

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
    List<T> listEqAndIn(SFunction<T, ?> eqColumn1, Object eqValue1,
                        SFunction<T, ?> eqColumn2, Object eqValue2,
                        SFunction<T, ?> inColumn, Collection<?> inValues);

}
