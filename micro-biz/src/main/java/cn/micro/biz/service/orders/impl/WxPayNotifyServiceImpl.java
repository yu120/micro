package cn.micro.biz.service.orders.impl;

import cn.micro.biz.entity.orders.WxPayNotifyEntity;
import cn.micro.biz.mapper.orders.IWxPayNotifyMapper;
import cn.micro.biz.service.orders.IWxPayNotifyService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * Wx Pay Notify Service Implements
 *
 * @author lry
 */
@Service
public class WxPayNotifyServiceImpl extends ServiceImpl<IWxPayNotifyMapper, WxPayNotifyEntity> implements IWxPayNotifyService {

}
