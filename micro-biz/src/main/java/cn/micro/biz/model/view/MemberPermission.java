package cn.micro.biz.model.view;

import cn.micro.biz.entity.member.MemberEntity;
import cn.micro.biz.entity.member.PermissionEntity;
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
public class MemberPermission extends PermissionEntity {

    /**
     * Member id
     *
     * @see MemberEntity#id
     */
    private Long memberId;

}
