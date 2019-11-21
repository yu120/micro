package cn.micro.biz.service.member.impl;

import cn.micro.biz.entity.member.PermissionEntity;
import cn.micro.biz.mapper.member.IPermissionMapper;
import cn.micro.biz.service.member.*;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Permission Service Implements
 *
 * @author lry
 */
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class PermissionServiceImpl extends ServiceImpl<IPermissionMapper, PermissionEntity> implements IPermissionService {

}
