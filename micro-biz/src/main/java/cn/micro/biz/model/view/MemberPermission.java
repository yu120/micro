package cn.micro.biz.model.view;

import cn.micro.biz.entity.member.Member;
import cn.micro.biz.entity.member.Permission;
import lombok.*;

/**
 * Member Permission
 *
 * @author lry
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class MemberPermission extends Permission {

    /**
     * Member id
     *
     * @see Member#id
     */
    private Long memberId;

}
