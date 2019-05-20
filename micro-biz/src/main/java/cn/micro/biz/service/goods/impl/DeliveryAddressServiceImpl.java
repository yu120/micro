package cn.micro.biz.service.goods.impl;

import cn.micro.biz.commons.mybatis.extension.MicroServiceImpl;
import cn.micro.biz.entity.goods.DeliveryAddress;
import cn.micro.biz.mapper.goods.IDeliveryAddressMapper;
import cn.micro.biz.service.goods.IDeliveryAddressService;
import org.springframework.stereotype.Service;

/**
 * Delivery Address Service Implements
 *
 * @author lry
 */
@Service
public class DeliveryAddressServiceImpl extends MicroServiceImpl<IDeliveryAddressMapper, DeliveryAddress> implements IDeliveryAddressService {

}
