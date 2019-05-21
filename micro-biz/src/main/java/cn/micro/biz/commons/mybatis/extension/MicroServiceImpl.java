package cn.micro.biz.commons.mybatis.extension;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

/**
 * Micro Service Implements
 *
 * @param <M>
 * @param <T>
 * @author lry
 */
public class MicroServiceImpl<M extends BaseMapper<T>, T extends Model<T>>
        extends ServiceImpl<M, T> implements IMicroService<T> {

}
