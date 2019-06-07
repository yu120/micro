package cn.micro.biz.service.safe.impl;

import cn.micro.biz.commons.mybatis.extension.MicroServiceImpl;
import cn.micro.biz.entity.safe.AuditLog;
import cn.micro.biz.mapper.safe.IAuditLogMapper;
import cn.micro.biz.service.safe.IAuditLogService;
import org.springframework.stereotype.Service;

/**
 * Audit Log Service Implements
 *
 * @author lry
 */
@Service
public class AuditLogServiceImpl extends MicroServiceImpl<IAuditLogMapper, AuditLog> implements IAuditLogService {

}
