package cn.micro.biz.mapper.member;

import cn.micro.biz.commons.mybatis.extension.IMicroMapper;
import cn.micro.biz.entity.member.Permission;
import cn.micro.biz.model.view.MemberPermission;

import java.util.List;

/**
 * Permission Mapper
 *
 * @author lry
 */
public interface IPermissionMapper extends IMicroMapper<Permission> {

    /**
     * The query all permission list
     *
     * @return {@link List<Permission>}
     */
    List<Permission> selectAllPermissions();

    /**
     * The query permission list by member id
     * <p>
     * Tip: Query View
     *
     * @param memberId {@link cn.micro.biz.entity.member.Member#id}
     * @return {@link List<MemberPermission>}
     */
    List<MemberPermission> selectPermissionsByMemberId(String memberId);

}