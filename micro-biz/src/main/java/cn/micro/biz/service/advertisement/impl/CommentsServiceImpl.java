package cn.micro.biz.service.advertisement.impl;

import cn.micro.biz.commons.mybatis.extension.MicroServiceImpl;
import cn.micro.biz.entity.advertisement.CommentsEntity;
import cn.micro.biz.mapper.advertisement.ICommentsMapper;
import cn.micro.biz.service.advertisement.ICommentsService;
import org.springframework.stereotype.Service;

/**
 * Comments Service Implements
 *
 * @author lry
 */
@Service
public class CommentsServiceImpl extends MicroServiceImpl<ICommentsMapper, CommentsEntity> implements ICommentsService {

}
