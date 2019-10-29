package cn.micro.biz.service.member.impl;

import cn.micro.biz.commons.auth.MicroAuthContext;
import cn.micro.biz.commons.mybatis.extension.MicroServiceImpl;
import cn.micro.biz.entity.member.MemberEntity;
import cn.micro.biz.mapper.member.IMemberMapper;
import cn.micro.biz.model.edit.EditMemberInfo;
import cn.micro.biz.service.member.IMemberService;
import com.github.dozermapper.core.Mapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Member Service Implements
 *
 * @author lry
 */
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class MemberServiceImpl extends MicroServiceImpl<IMemberMapper, MemberEntity> implements IMemberService {

    private final Mapper mapper;

    @Override
    public MemberEntity info() {
        Long memberId = MicroAuthContext.getMemberId();
        return super.getById(memberId).desensitization();
    }

    @Override
    public Boolean edit(EditMemberInfo memberInfo) {
        MemberEntity member = mapper.map(memberInfo, MemberEntity.class);
        member.setId(MicroAuthContext.getMemberId());
        return super.updateById(member);
    }

}
