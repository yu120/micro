package cn.micro.biz.service.safe.impl;

import cn.micro.biz.commons.mybatis.extension.MicroServiceImpl;
import cn.micro.biz.entity.safe.LoginLog;
import cn.micro.biz.mapper.safe.ILoginLogMapper;
import cn.micro.biz.service.safe.ILoginLogService;
import org.springframework.stereotype.Service;

/**
 * Login Log Service Implements
 *
 * @author lry
 */
@Service
public class LoginLogServiceImpl extends MicroServiceImpl<ILoginLogMapper, LoginLog> implements ILoginLogService {

}
