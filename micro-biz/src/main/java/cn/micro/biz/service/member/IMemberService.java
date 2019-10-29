package cn.micro.biz.service.member;

import cn.micro.biz.commons.mybatis.extension.IMicroService;
import cn.micro.biz.entity.member.MemberEntity;
import cn.micro.biz.model.edit.EditMemberInfo;

/**
 * Member Service
 *
 * @author lry
 */
public interface IMemberService extends IMicroService<MemberEntity> {

    /**
     * The get current member info
     *
     * @return {@link MemberEntity}
     */
    MemberEntity info();

    /**
     * Edit Member Info
     *
     * @param memberInfo {@link EditMemberInfo}
     * @return success return true
     */
    Boolean edit(EditMemberInfo memberInfo);

}
