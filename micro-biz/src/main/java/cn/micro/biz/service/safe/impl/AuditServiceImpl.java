package cn.micro.biz.service.safe.impl;

import cn.micro.biz.commons.mybatis.extension.MicroServiceImpl;
import cn.micro.biz.entity.safe.Audit;
import cn.micro.biz.mapper.safe.IAuditMapper;
import cn.micro.biz.service.safe.IAuditService;
import org.springframework.stereotype.Service;

/**
 * Audit Service Implements
 *
 * @author lry
 */
@Service
public class AuditServiceImpl extends MicroServiceImpl<IAuditMapper, Audit> implements IAuditService {

}
