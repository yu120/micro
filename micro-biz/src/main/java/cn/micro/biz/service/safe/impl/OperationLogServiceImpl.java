package cn.micro.biz.service.safe.impl;

import cn.micro.biz.commons.mybatis.extension.MicroServiceImpl;
import cn.micro.biz.entity.safe.OperationLog;
import cn.micro.biz.mapper.safe.IOperationLogMapper;
import cn.micro.biz.service.safe.IOperationLogService;
import org.springframework.stereotype.Service;

/**
 * Operation Log Service Implements
 *
 * @author lry
 */
@Service
public class OperationLogServiceImpl extends MicroServiceImpl<IOperationLogMapper, OperationLog> implements IOperationLogService {

}
