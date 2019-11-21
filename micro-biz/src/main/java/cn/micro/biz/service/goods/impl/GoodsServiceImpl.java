package cn.micro.biz.service.goods.impl;

import cn.micro.biz.entity.goods.GoodsEntity;
import cn.micro.biz.mapper.goods.IGoodsMapper;
import cn.micro.biz.service.goods.IGoodsService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * Goods Service Implements
 *
 * @author lry
 */
@Service
public class GoodsServiceImpl extends ServiceImpl<IGoodsMapper, GoodsEntity> implements IGoodsService {

}
