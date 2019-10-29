package cn.micro.biz.service.orders.impl;

import cn.micro.biz.commons.mybatis.extension.MicroServiceImpl;
import cn.micro.biz.entity.orders.WxPayNotifyEntity;
import cn.micro.biz.mapper.orders.IWxPayNotifyMapper;
import cn.micro.biz.service.orders.IWxPayNotifyService;
import org.springframework.stereotype.Service;

/**
 * Wx Pay Notify Service Implements
 *
 * @author lry
 */
@Service
public class WxPayNotifyServiceImpl extends MicroServiceImpl<IWxPayNotifyMapper, WxPayNotifyEntity> implements IWxPayNotifyService {

}
