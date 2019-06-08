package cn.micro.biz.service.order.impl;

import cn.micro.biz.commons.mybatis.extension.MicroServiceImpl;
import cn.micro.biz.entity.order.AliPayNotify;
import cn.micro.biz.mapper.order.IAliPayNotifyMapper;
import cn.micro.biz.service.order.IAliPayNotifyService;
import org.springframework.stereotype.Service;

/**
 * Ali Pay Notify Service Implements
 *
 * @author lry
 */
@Service
public class AliPayNotifyServiceImpl extends MicroServiceImpl<IAliPayNotifyMapper, AliPayNotify> implements IAliPayNotifyService {

}
