package cn.micro.biz.service.goods.impl;

import cn.micro.biz.commons.mybatis.extension.MicroServiceImpl;
import cn.micro.biz.entity.comments.Comments;
import cn.micro.biz.mapper.goods.ICommentsMapper;
import cn.micro.biz.service.goods.ICommentsService;
import org.springframework.stereotype.Service;

/**
 * Goods Comments Service Implements
 *
 * @author lry
 */
@Service
public class CommentsServiceImpl extends MicroServiceImpl<ICommentsMapper, Comments> implements ICommentsService {

}
