package cn.micro.biz.service.goods.impl;

import cn.micro.biz.commons.mybatis.extension.MicroServiceImpl;
import cn.micro.biz.entity.comments.Behavior;
import cn.micro.biz.mapper.goods.IBehaviorMapper;
import cn.micro.biz.service.goods.IBehaviorService;
import org.springframework.stereotype.Service;

/**
 * Behavior Service Implements
 *
 * @author lry
 */
@Service
public class BehaviorServiceImpl extends MicroServiceImpl<IBehaviorMapper, Behavior> implements IBehaviorService {

}
