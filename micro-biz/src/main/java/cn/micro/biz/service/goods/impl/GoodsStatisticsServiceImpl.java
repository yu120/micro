package cn.micro.biz.service.goods.impl;

import cn.micro.biz.commons.mybatis.extension.MicroServiceImpl;
import cn.micro.biz.entity.goods.GoodsStatistics;
import cn.micro.biz.mapper.goods.IGoodsStatisticsMapper;
import cn.micro.biz.service.goods.IGoodsStatisticsService;
import org.springframework.stereotype.Service;

/**
 * Goods Statistics Service Implements
 *
 * @author lry
 */
@Service
public class GoodsStatisticsServiceImpl extends MicroServiceImpl<IGoodsStatisticsMapper,
        GoodsStatistics> implements IGoodsStatisticsService {

}
