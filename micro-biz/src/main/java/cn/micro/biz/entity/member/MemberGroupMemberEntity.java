package cn.micro.biz.entity.member;

import cn.micro.biz.commons.mybatis.MicroEntity;
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
public class MemberGroupMemberEntity extends MicroEntity<MemberGroupMemberEntity> {

    private static final long serialVersionUID = 1L;

    /**
     * Member group id
     *
     * @see MemberGroupEntity#id
     */
    private Long memberGroupId;

    /**
     * Member id
     *
     * @see MemberEntity#id
     */
    private Long memberId;

}
