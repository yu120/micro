package cn.micro.biz.service.orders.impl;

import cn.micro.biz.commons.mybatis.extension.MicroServiceImpl;
import cn.micro.biz.entity.orders.AliPayNotifyEntity;
import cn.micro.biz.mapper.orders.IAliPayNotifyMapper;
import cn.micro.biz.service.orders.IAliPayNotifyService;
import org.springframework.stereotype.Service;

/**
 * Ali Pay Notify Service Implements
 *
 * @author lry
 */
@Service
public class AliPayNotifyServiceImpl extends MicroServiceImpl<IAliPayNotifyMapper, AliPayNotifyEntity> implements IAliPayNotifyService {

}
