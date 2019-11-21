package cn.micro.biz.mapper.member;

import cn.micro.biz.entity.member.MemberEntity;
import cn.micro.biz.entity.member.PermissionEntity;
import cn.micro.biz.model.view.MemberPermission;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;

/**
 * Permission Mapper
 *
 * @author lry
 */
public interface IPermissionMapper extends BaseMapper<PermissionEntity> {

    /**
     * The query permission list by member id
     * <p>
     * Tip: Query View
     *
     * @param memberId {@link MemberEntity#id}
     * @return {@link List<MemberPermission>}
     */
    List<MemberPermission> selectPermissionsByMemberId(String memberId);

}