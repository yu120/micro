package cn.micro.biz.service.order.impl;

import cn.micro.biz.commons.mybatis.extension.MicroServiceImpl;
import cn.micro.biz.entity.order.OrderLogisticsEntity;
import cn.micro.biz.mapper.order.IOrderLogisticsMapper;
import cn.micro.biz.service.order.IOrderLogisticsService;
import org.springframework.stereotype.Service;

/**
 * Order Logistics Service Implements
 *
 * @author lry
 */
@Service
public class OrderLogisticsServiceImpl extends MicroServiceImpl<IOrderLogisticsMapper, OrderLogisticsEntity> implements IOrderLogisticsService {

}
