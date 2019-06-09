package cn.micro.biz.service.unified.impl;

import cn.micro.biz.commons.mybatis.extension.MicroServiceImpl;
import cn.micro.biz.entity.unified.LoginLog;
import cn.micro.biz.mapper.unified.ILoginLogMapper;
import cn.micro.biz.service.unified.ILoginLogService;
import org.springframework.stereotype.Service;

/**
 * Login Log Service Implements
 *
 * @author lry
 */
@Service
public class LoginLogServiceImpl extends MicroServiceImpl<ILoginLogMapper, LoginLog> implements ILoginLogService {

}
