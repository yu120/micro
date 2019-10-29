package cn.micro.biz.service.unified.impl;

import cn.micro.biz.commons.mybatis.extension.MicroServiceImpl;
import cn.micro.biz.entity.unified.AppEntity;
import cn.micro.biz.mapper.unified.IAppMapper;
import cn.micro.biz.service.unified.IAppService;
import org.springframework.stereotype.Service;

/**
 * App Service Implements
 *
 * @author lry
 */
@Service
public class AppServiceImpl extends MicroServiceImpl<IAppMapper, AppEntity> implements IAppService {

}
