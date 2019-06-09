package cn.micro.biz.service.advertisement.impl;

import cn.micro.biz.commons.mybatis.extension.MicroServiceImpl;
import cn.micro.biz.entity.advertisement.AdvertisementStore;
import cn.micro.biz.mapper.advertisement.IAdvertisementStoreMapper;
import cn.micro.biz.service.advertisement.IAdvertisementStoreService;
import org.springframework.stereotype.Service;

/**
 * Advertisement Store Service Implements
 *
 * @author lry
 */
@Service
public class AdvertisementStoreServiceImpl extends MicroServiceImpl<IAdvertisementStoreMapper,
        AdvertisementStore> implements IAdvertisementStoreService {

}
