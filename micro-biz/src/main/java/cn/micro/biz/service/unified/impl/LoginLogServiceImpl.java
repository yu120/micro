package cn.micro.biz.service.unified.impl;

import cn.micro.biz.entity.unified.LoginLogEntity;
import cn.micro.biz.mapper.unified.ILoginLogMapper;
import cn.micro.biz.service.unified.ILoginLogService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * Login Log Service Implements
 *
 * @author lry
 */
@Service
public class LoginLogServiceImpl extends ServiceImpl<ILoginLogMapper, LoginLogEntity> implements ILoginLogService {

}
