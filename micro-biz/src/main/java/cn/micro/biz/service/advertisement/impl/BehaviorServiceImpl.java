package cn.micro.biz.service.advertisement.impl;

import cn.micro.biz.commons.mybatis.extension.MicroServiceImpl;
import cn.micro.biz.entity.advertisement.Behavior;
import cn.micro.biz.mapper.advertisement.IBehaviorMapper;
import cn.micro.biz.service.advertisement.IBehaviorService;
import org.springframework.stereotype.Service;

/**
 * Behavior Service Implements
 *
 * @author lry
 */
@Service
public class BehaviorServiceImpl extends MicroServiceImpl<IBehaviorMapper, Behavior> implements IBehaviorService {

}
