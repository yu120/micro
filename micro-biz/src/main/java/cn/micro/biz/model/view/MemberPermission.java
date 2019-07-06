package cn.micro.biz.model.view;

import cn.micro.biz.entity.member.Member;
import cn.micro.biz.entity.member.Permission;

/**
 * Member Permission
 *
 * @author lry
 */
public class MemberPermission extends Permission {

    /**
     * Member id
     *
     * @see Member#id
     */
    private Long memberId;

}
