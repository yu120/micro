package cn.micro.biz.service.unified.impl;

import cn.micro.biz.entity.unified.AppEntity;
import cn.micro.biz.mapper.unified.IAppMapper;
import cn.micro.biz.service.unified.IAppService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * App Service Implements
 *
 * @author lry
 */
@Service
public class AppServiceImpl extends ServiceImpl<IAppMapper, AppEntity> implements IAppService{

}
