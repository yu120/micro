package cn.micro.biz.service.member.impl;

import cn.micro.biz.commons.mybatis.extension.MicroServiceImpl;
import cn.micro.biz.entity.member.Area;
import cn.micro.biz.mapper.member.IAreaMapper;
import cn.micro.biz.service.member.IAreaService;
import org.springframework.stereotype.Service;

/**
 * Area Service Implements
 *
 * @author lry
 */
@Service
public class AreaServiceImpl extends MicroServiceImpl<IAreaMapper, Area> implements IAreaService {

}
