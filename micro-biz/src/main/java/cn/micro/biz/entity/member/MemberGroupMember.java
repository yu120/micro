package cn.micro.biz.entity.member;

import cn.micro.biz.commons.mybatis.entity.MicroEntity;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.*;

/**
 * Member Group to Member Entity
 *
 * @author lry
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@TableName("member_group_member")
public class MemberGroupMember extends MicroEntity<MemberGroupMember> {

    private static final long serialVersionUID = 1L;

    /**
     * Member group id
     * <p>
     * {@link MemberGroup#id}
     */
    private Long memberGroupId;

    /**
     * Member id
     * <p>
     * {@link Member#id}
     */
    private Long memberId;

}
