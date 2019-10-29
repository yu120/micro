package cn.micro.biz.service.advertisement.impl;

import cn.micro.biz.commons.mybatis.extension.MicroServiceImpl;
import cn.micro.biz.entity.advertisement.AdvertisementPositionEntity;
import cn.micro.biz.mapper.advertisement.IAdvertisementPositionMapper;
import cn.micro.biz.service.advertisement.IAdvertisementPositionService;
import org.springframework.stereotype.Service;

/**
 * Advertisement Position Service Implements
 *
 * @author lry
 */
@Service
public class AdvertisementPositionServiceImpl extends MicroServiceImpl<IAdvertisementPositionMapper,
        AdvertisementPositionEntity> implements IAdvertisementPositionService {

}
