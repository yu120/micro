package cn.micro.biz.service.advertisement.impl;

import cn.micro.biz.entity.advertisement.AdvertisementEntity;
import cn.micro.biz.mapper.advertisement.IAdvertisementMapper;
import cn.micro.biz.service.advertisement.IAdvertisementService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * Advertisement Service Implements
 *
 * @author lry
 */
@Service
public class AdvertisementServiceImpl extends ServiceImpl<IAdvertisementMapper,AdvertisementEntity> implements IAdvertisementService {

}
