package cn.micro.biz.service.safe.impl;

import cn.micro.biz.commons.mybatis.extension.MicroServiceImpl;
import cn.micro.biz.entity.safe.AuditLog;
import cn.micro.biz.mapper.safe.IOperationLogMapper;
import cn.micro.biz.service.safe.IOperationLogService;
import org.springframework.stereotype.Service;

/**
 * Audit Log Service Implements
 *
 * @author lry
 */
@Service
public class OperationLogServiceImpl extends MicroServiceImpl<IOperationLogMapper, AuditLog> implements IOperationLogService {

}
