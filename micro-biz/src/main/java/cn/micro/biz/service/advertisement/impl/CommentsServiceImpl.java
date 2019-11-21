package cn.micro.biz.service.advertisement.impl;

import cn.micro.biz.entity.advertisement.CommentsEntity;
import cn.micro.biz.mapper.advertisement.ICommentsMapper;
import cn.micro.biz.service.advertisement.ICommentsService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * Comments Service Implements
 *
 * @author lry
 */
@Service
public class CommentsServiceImpl extends ServiceImpl<ICommentsMapper, CommentsEntity> implements ICommentsService {

}
