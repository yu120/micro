package cn.micro.biz.commons.mybatis.extension;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.toolkit.support.SFunction;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import java.util.Collection;
import java.util.List;

/**
 * Micro Service Impl
 *
 * @param <M>
 * @param <T>
 * @author lry
 */
public class MicroServiceImpl<M extends BaseMapper<T>, T extends Model<T>> extends ServiceImpl<M, T>
        implements IMicroService<T> {

    // ====== lambda equals(eq) to one object

    @Override
    public T getOneEqs(SFunction<T, ?> column, Object value) {
        return getOneEqs(column, value, null, null);
    }

    @Override
    public T getOneEqs(SFunction<T, ?> column1, Object value1,
                       SFunction<T, ?> column2, Object value2) {
        return getOneEqs(column1, value1, column2, value2, null, null);
    }

    @Override
    public T getOneEqs(SFunction<T, ?> column1, Object value1,
                       SFunction<T, ?> column2, Object value2,
                       SFunction<T, ?> column3, Object value3) {
        return getOne(buildEq(column1, value1, column2, value2, column3, value3));
    }

    // ====== lambda equals(eq) to list object

    @Override
    public List<T> listEqs(SFunction<T, ?> column, Object value) {
        return listEqs(column, value, null, null);
    }

    @Override
    public List<T> listEqs(SFunction<T, ?> column1, Object value1,
                           SFunction<T, ?> column2, Object value2) {
        return listEqs(column1, value1, column2, value2, null, null);
    }

    @Override
    public List<T> listEqs(SFunction<T, ?> column1, Object value1,
                           SFunction<T, ?> column2, Object value2,
                           SFunction<T, ?> column3, Object value3) {
        return list(buildEq(column1, value1, column2, value2, column3, value3));
    }

    // ====== lambda in to list object

    @Override
    public List<T> listIns(SFunction<T, ?> column, Object... values) {
        return list(new LambdaQueryWrapper<T>().in(column, values));
    }

    @Override
    public List<T> listIns(SFunction<T, ?> column, Collection<?> values) {
        return list(new LambdaQueryWrapper<T>().in(column, values));
    }

    // ====== lambda equals(eq) and in to list object

    @Override
    public List<T> listEqAndIn(
            SFunction<T, ?> eqColumn, Object eqValue,
            SFunction<T, ?> inColumn, Object... inValues) {
        return listEqAndIn(eqColumn, eqValue, null, null, inColumn, inValues);
    }

    @Override
    public List<T> listEqAndIn(
            SFunction<T, ?> eqColumn1, Object eqValue1,
            SFunction<T, ?> eqColumn2, Object eqValue2,
            SFunction<T, ?> inColumn, Object... inValues) {
        return list(buildEqIn(eqColumn1, eqValue1, eqColumn2, eqValue2).in(inColumn, inValues));
    }

    @Override
    public List<T> listEqAndIn(
            SFunction<T, ?> eqColumn, Object eqValue,
            SFunction<T, ?> inColumn, Collection<?> inValues) {
        return listEqAndIn(eqColumn, eqValue, null, null, inColumn, inValues);
    }

    @Override
    public List<T> listEqAndIn(
            SFunction<T, ?> eqColumn1, Object eqValue1,
            SFunction<T, ?> eqColumn2, Object eqValue2,
            SFunction<T, ?> inColumn, Collection<?> inValues) {
        return list(buildEqIn(eqColumn1, eqValue1, eqColumn2, eqValue2).in(inColumn, inValues));
    }

    private LambdaQueryWrapper<T> buildEq(
            SFunction<T, ?> column1, Object value1,
            SFunction<T, ?> column2, Object value2,
            SFunction<T, ?> column3, Object value3) {
        LambdaQueryWrapper<T> wrapper = new LambdaQueryWrapper<T>().eq(column1, value1);
        if (!(column2 == null || value2 == null)) {
            wrapper.eq(column2, value2);
        }
        if (!(column3 == null || value3 == null)) {
            wrapper.eq(column3, value3);
        }

        return wrapper;
    }

    private LambdaQueryWrapper<T> buildEqIn(
            SFunction<T, ?> eqColumn1, Object eqValue1,
            SFunction<T, ?> eqColumn2, Object eqValue2) {
        LambdaQueryWrapper<T> wrapper = new LambdaQueryWrapper<T>().eq(eqColumn1, eqValue1);
        if (!(eqColumn2 == null || eqValue2 == null)) {
            wrapper.eq(eqColumn2, eqValue2);
        }

        return wrapper;
    }

}
