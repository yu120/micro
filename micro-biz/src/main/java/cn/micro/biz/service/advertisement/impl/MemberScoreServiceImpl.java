package cn.micro.biz.service.advertisement.impl;

import cn.micro.biz.commons.mybatis.extension.MicroServiceImpl;
import cn.micro.biz.entity.advertisement.MemberScore;
import cn.micro.biz.mapper.advertisement.IMemberScoreMapper;
import cn.micro.biz.service.advertisement.IMemberScoreService;
import org.springframework.stereotype.Service;

/**
 * Member Score Service Implements
 *
 * @author lry
 */
@Service
public class MemberScoreServiceImpl extends MicroServiceImpl<IMemberScoreMapper,
        MemberScore> implements IMemberScoreService {

}
