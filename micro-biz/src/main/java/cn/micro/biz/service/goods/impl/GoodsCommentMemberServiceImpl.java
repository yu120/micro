package cn.micro.biz.service.goods.impl;

import cn.micro.biz.commons.mybatis.extension.MicroServiceImpl;
import cn.micro.biz.entity.comment.CommentBehavior;
import cn.micro.biz.mapper.goods.IGoodsCommentMemberMapper;
import cn.micro.biz.service.goods.IGoodsCommentMemberService;
import org.springframework.stereotype.Service;

/**
 * Goods Comment Member Service Implements
 *
 * @author lry
 */
@Service
public class GoodsCommentMemberServiceImpl extends MicroServiceImpl<IGoodsCommentMemberMapper, CommentBehavior> implements IGoodsCommentMemberService {

}
