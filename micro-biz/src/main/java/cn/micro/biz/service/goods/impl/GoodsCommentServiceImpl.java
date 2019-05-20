package cn.micro.biz.service.goods.impl;

import cn.micro.biz.commons.mybatis.extension.MicroServiceImpl;
import cn.micro.biz.entity.goods.GoodsComment;
import cn.micro.biz.mapper.goods.IGoodsCommentMapper;
import cn.micro.biz.service.goods.IGoodsCommentService;
import org.springframework.stereotype.Service;

/**
 * Goods Comment Service Implements
 *
 * @author lry
 */
@Service
public class GoodsCommentServiceImpl extends MicroServiceImpl<IGoodsCommentMapper, GoodsComment> implements IGoodsCommentService {

}
