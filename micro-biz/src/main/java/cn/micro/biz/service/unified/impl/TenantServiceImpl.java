package cn.micro.biz.service.unified.impl;

import cn.micro.biz.entity.unified.TenantEntity;
import cn.micro.biz.mapper.unified.ITenantMapper;
import cn.micro.biz.service.unified.ITenantService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * Tenant Service Implements
 *
 * @author lry
 */
@Service
public class TenantServiceImpl extends ServiceImpl<ITenantMapper, TenantEntity> implements ITenantService {

}
