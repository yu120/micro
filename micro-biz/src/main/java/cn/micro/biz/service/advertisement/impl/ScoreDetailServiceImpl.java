package cn.micro.biz.service.advertisement.impl;

import cn.micro.biz.commons.mybatis.extension.MicroServiceImpl;
import cn.micro.biz.entity.advertisement.ScoreDetailEntity;
import cn.micro.biz.mapper.advertisement.IScoreDetailMapper;
import cn.micro.biz.service.advertisement.IScoreDetailService;
import org.springframework.stereotype.Service;

/**
 * Score Detail Service Implements
 *
 * @author lry
 */
@Service
public class ScoreDetailServiceImpl extends MicroServiceImpl<IScoreDetailMapper,
        ScoreDetailEntity> implements IScoreDetailService {

}
