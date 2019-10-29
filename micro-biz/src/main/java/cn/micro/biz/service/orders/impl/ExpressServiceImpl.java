package cn.micro.biz.service.orders.impl;

import cn.micro.biz.commons.mybatis.extension.MicroServiceImpl;
import cn.micro.biz.entity.orders.ExpressEntity;
import cn.micro.biz.mapper.orders.IExpressMapper;
import cn.micro.biz.service.orders.IExpressService;
import org.springframework.stereotype.Service;

/**
 * Express Service Implements
 *
 * @author lry
 */
@Service
public class ExpressServiceImpl extends MicroServiceImpl<IExpressMapper, ExpressEntity> implements IExpressService {

}
