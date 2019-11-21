package cn.micro.biz.service.unified.impl;

import cn.micro.biz.entity.unified.AuditEntity;
import cn.micro.biz.mapper.unified.IAuditMapper;
import cn.micro.biz.service.unified.IAuditService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * Audit Service Implements
 *
 * @author lry
 */
@Service
public class AuditServiceImpl extends ServiceImpl<IAuditMapper, AuditEntity> implements IAuditService {

}
