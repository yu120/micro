package cn.micro.biz.service.goods.impl;

import cn.micro.biz.commons.mybatis.extension.MicroServiceImpl;
import cn.micro.biz.entity.goods.GoodsCommentMember;
import cn.micro.biz.mapper.goods.IGoodsCommentMemberMapper;
import cn.micro.biz.service.goods.IGoodsCommentMemberService;
import org.springframework.stereotype.Service;

@Service
public class GoodsCommentMemberServiceImpl extends MicroServiceImpl<IGoodsCommentMemberMapper, GoodsCommentMember> implements IGoodsCommentMemberService {

}
