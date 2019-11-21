package cn.micro.biz.service.member.impl;

import cn.micro.biz.entity.member.MemberGroupEntity;
import cn.micro.biz.mapper.member.IMemberGroupMapper;
import cn.micro.biz.service.member.IMemberGroupService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Member Group Service Implements
 *
 * @author lry
 */
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class MemberGroupServiceImpl extends ServiceImpl<IMemberGroupMapper, MemberGroupEntity> implements IMemberGroupService {

}
