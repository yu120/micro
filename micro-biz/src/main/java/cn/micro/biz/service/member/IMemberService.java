package cn.micro.biz.service.member;

import cn.micro.biz.entity.member.MemberEntity;
import cn.micro.biz.model.edit.EditMemberInfo;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * Member Service
 *
 * @author lry
 */
public interface IMemberService extends IService<MemberEntity> {

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
