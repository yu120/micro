package cn.micro.biz.entity.member;

import cn.micro.biz.commons.mybatis.MicroEntity;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.*;

/**
 * Member Group to Role Entity
 *
 * @author lry
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@TableName("member_group_role")
public class MemberGroupRoleEntity extends MicroEntity<MemberGroupRoleEntity> {

    private static final long serialVersionUID = 1L;

    /**
     * Member group id
     *
     * @see MemberGroupEntity#id
     */
    private Long memberGroupId;

    /**
     * Role id
     *
     * @see RoleEntity#id
     */
    private Long roleId;

}
