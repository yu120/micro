package cn.micro.biz.service.safe.impl;

import cn.micro.biz.commons.mybatis.extension.MicroServiceImpl;
import cn.micro.biz.entity.safe.AuditRecord;
import cn.micro.biz.mapper.safe.IAuditRecordMapper;
import cn.micro.biz.service.safe.IAuditRecordService;
import org.springframework.stereotype.Service;

/**
 * Audit Record Service Implements
 *
 * @author lry
 */
@Service
public class AuditRecordServiceImpl extends MicroServiceImpl<IAuditRecordMapper, AuditRecord> implements IAuditRecordService {

}
