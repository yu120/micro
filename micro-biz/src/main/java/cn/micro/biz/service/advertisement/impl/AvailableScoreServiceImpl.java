package cn.micro.biz.service.advertisement.impl;

import cn.micro.biz.commons.mybatis.extension.MicroServiceImpl;
import cn.micro.biz.entity.advertisement.AvailableScoreEntity;
import cn.micro.biz.mapper.advertisement.IAvailableScoreMapper;
import cn.micro.biz.service.advertisement.IAvailableScoreService;
import org.springframework.stereotype.Service;

/**
 * Available Score Service Implements
 *
 * @author lry
 */
@Service
public class AvailableScoreServiceImpl extends MicroServiceImpl<IAvailableScoreMapper,
        AvailableScoreEntity> implements IAvailableScoreService {

}
