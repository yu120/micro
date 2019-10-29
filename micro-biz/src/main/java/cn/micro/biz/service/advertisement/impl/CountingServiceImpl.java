package cn.micro.biz.service.advertisement.impl;

import cn.micro.biz.commons.mybatis.extension.MicroServiceImpl;
import cn.micro.biz.entity.advertisement.CountingEntity;
import cn.micro.biz.mapper.advertisement.ICountingMapper;
import cn.micro.biz.service.advertisement.ICountingService;
import org.springframework.stereotype.Service;

/**
 * Counting Service Implements
 *
 * @author lry
 */
@Service
public class CountingServiceImpl extends MicroServiceImpl<ICountingMapper, CountingEntity> implements ICountingService {

}
