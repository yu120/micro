package cn.micro.biz.service.unified.impl;

import cn.micro.biz.commons.mybatis.extension.MicroServiceImpl;
import cn.micro.biz.entity.unified.Audit;
import cn.micro.biz.mapper.unified.IAuditMapper;
import cn.micro.biz.service.unified.IAuditService;
import org.springframework.stereotype.Service;

/**
 * Audit Service Implements
 *
 * @author lry
 */
@Service
public class AuditServiceImpl extends MicroServiceImpl<IAuditMapper, Audit> implements IAuditService {

}
