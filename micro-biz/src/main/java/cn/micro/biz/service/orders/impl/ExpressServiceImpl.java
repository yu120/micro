package cn.micro.biz.service.orders.impl;

import cn.micro.biz.entity.orders.ExpressEntity;
import cn.micro.biz.mapper.orders.IExpressMapper;
import cn.micro.biz.service.orders.IExpressService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * Express Service Implements
 *
 * @author lry
 */
@Service
public class ExpressServiceImpl extends ServiceImpl<IExpressMapper, ExpressEntity> implements IExpressService {

}
