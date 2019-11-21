package cn.micro.biz.service.orders.impl;

import cn.micro.biz.entity.orders.AliPayNotifyEntity;
import cn.micro.biz.mapper.orders.IAliPayNotifyMapper;
import cn.micro.biz.service.orders.IAliPayNotifyService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * Ali Pay Notify Service Implements
 *
 * @author lry
 */
@Service
public class AliPayNotifyServiceImpl extends ServiceImpl<IAliPayNotifyMapper, AliPayNotifyEntity> implements IAliPayNotifyService {

}
