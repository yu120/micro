package cn.micro.biz.service.safe.impl;

import cn.micro.biz.commons.mybatis.extension.MicroServiceImpl;
import cn.micro.biz.entity.safe.Audit;
import cn.micro.biz.mapper.safe.IOperationMapper;
import cn.micro.biz.service.safe.IOperationService;
import org.springframework.stereotype.Service;

/**
 * Audit Service Implements
 *
 * @author lry
 */
@Service
public class OperationServiceImpl extends MicroServiceImpl<IOperationMapper, Audit> implements IOperationService {

}
