package cn.micro.biz.service.unified.impl;

import cn.micro.biz.commons.mybatis.extension.MicroServiceImpl;
import cn.micro.biz.entity.unified.AuditRecordEntity;
import cn.micro.biz.mapper.unified.IAuditRecordMapper;
import cn.micro.biz.service.unified.IAuditRecordService;
import org.springframework.stereotype.Service;

/**
 * Audit Record Service Implements
 *
 * @author lry
 */
@Service
public class AuditRecordServiceImpl extends MicroServiceImpl<IAuditRecordMapper, AuditRecordEntity> implements IAuditRecordService {

}
