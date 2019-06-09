package cn.micro.biz.service.unified.impl;

import cn.micro.biz.commons.mybatis.extension.MicroServiceImpl;
import cn.micro.biz.entity.unified.Area;
import cn.micro.biz.mapper.unified.IAreaMapper;
import cn.micro.biz.service.unified.IAreaService;
import org.springframework.stereotype.Service;

/**
 * Area Service Implements
 *
 * @author lry
 */
@Service
public class AreaServiceImpl extends MicroServiceImpl<IAreaMapper, Area> implements IAreaService {

}
