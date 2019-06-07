package cn.micro.biz.service.member;

import cn.micro.biz.commons.mybatis.extension.IMicroService;
import cn.micro.biz.entity.member.Member;
import cn.micro.biz.model.edit.EditMemberInfo;

/**
 * Member Service
 *
 * @author lry
 */
public interface IMemberService extends IMicroService<Member> {

    /**
     * The get current member info
     *
     * @return {@link Member}
     */
    Member info();

    /**
     * Edit Member Info
     *
     * @param memberInfo {@link EditMemberInfo}
     * @return
     */
    Boolean edit(EditMemberInfo memberInfo);

}
