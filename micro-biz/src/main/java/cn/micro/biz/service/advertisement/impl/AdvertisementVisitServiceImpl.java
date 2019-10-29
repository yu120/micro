package cn.micro.biz.service.advertisement.impl;

import cn.micro.biz.commons.mybatis.extension.MicroServiceImpl;
import cn.micro.biz.entity.advertisement.AdvertisementVisitEntity;
import cn.micro.biz.mapper.advertisement.IAdvertisementVisitMapper;
import cn.micro.biz.service.advertisement.IAdvertisementVisitService;
import org.springframework.stereotype.Service;

/**
 * Advertisement Visit Service Implements
 *
 * @author lry
 */
@Service
public class AdvertisementVisitServiceImpl extends MicroServiceImpl<IAdvertisementVisitMapper,
        AdvertisementVisitEntity> implements IAdvertisementVisitService {

}
