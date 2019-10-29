package cn.micro.biz.service.order.impl;

import cn.micro.biz.commons.mybatis.extension.MicroServiceImpl;
import cn.micro.biz.entity.order.ExpressEntity;
import cn.micro.biz.mapper.order.IExpressMapper;
import cn.micro.biz.service.order.IExpressService;
import org.springframework.stereotype.Service;

/**
 * Express Service Implements
 *
 * @author lry
 */
@Service
public class ExpressServiceImpl extends MicroServiceImpl<IExpressMapper, ExpressEntity> implements IExpressService {

}
