package cn.micro.biz.service.order.impl;

import cn.micro.biz.commons.mybatis.extension.MicroServiceImpl;
import cn.micro.biz.entity.order.WxPayNotifyEntity;
import cn.micro.biz.mapper.order.IWxPayNotifyMapper;
import cn.micro.biz.service.order.IWxPayNotifyService;
import org.springframework.stereotype.Service;

/**
 * Wx Pay Notify Service Implements
 *
 * @author lry
 */
@Service
public class WxPayNotifyServiceImpl extends MicroServiceImpl<IWxPayNotifyMapper, WxPayNotifyEntity> implements IWxPayNotifyService {

}
