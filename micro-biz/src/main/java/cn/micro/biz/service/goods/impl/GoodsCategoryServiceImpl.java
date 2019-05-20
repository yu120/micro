package cn.micro.biz.service.goods.impl;

import cn.micro.biz.commons.mybatis.extension.MicroServiceImpl;
import cn.micro.biz.entity.goods.GoodsCategory;
import cn.micro.biz.mapper.goods.IGoodsCategoryMapper;
import cn.micro.biz.service.goods.IGoodsCategoryService;
import org.springframework.stereotype.Service;

/**
 * Goods Category Service Implements
 *
 * @author lry
 */
@Service
public class GoodsCategoryServiceImpl extends MicroServiceImpl<IGoodsCategoryMapper, GoodsCategory> implements IGoodsCategoryService {

}
