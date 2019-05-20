package cn.micro.biz.service.order.impl;

import cn.micro.biz.commons.mybatis.extension.MicroServiceImpl;
import cn.micro.biz.entity.order.OrderLogistics;
import cn.micro.biz.mapper.order.IOrderLogisticsMapper;
import cn.micro.biz.service.order.IOrderLogisticsService;
import org.springframework.stereotype.Service;

@Service
public class OrderLogisticsServiceImpl extends MicroServiceImpl<IOrderLogisticsMapper, OrderLogistics> implements IOrderLogisticsService {

}
