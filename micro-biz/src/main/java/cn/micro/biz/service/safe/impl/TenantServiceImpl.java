package cn.micro.biz.service.safe.impl;

import cn.micro.biz.commons.mybatis.extension.MicroServiceImpl;
import cn.micro.biz.entity.unified.Tenant;
import cn.micro.biz.mapper.safe.ITenantMapper;
import cn.micro.biz.service.safe.ITenantService;
import org.springframework.stereotype.Service;

/**
 * Tenant Service Implements
 *
 * @author lry
 */
@Service
public class TenantServiceImpl extends MicroServiceImpl<ITenantMapper, Tenant> implements ITenantService {

}
