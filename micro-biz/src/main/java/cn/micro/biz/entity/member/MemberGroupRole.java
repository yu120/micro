package cn.micro.biz.entity.member;

import cn.micro.biz.commons.mybatis.entity.MicroEntity;
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
public class MemberGroupRole extends MicroEntity<MemberGroupRole> {

    private static final long serialVersionUID = 1L;

    /**
     * Member group id
     * <p>
     * {@link MemberGroup#id}
     */
    private Long memberGroupId;

    /**
     * Role id
     * <p>
     * {@link Role#id}
     */
    private Long roleId;

}
