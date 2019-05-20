package cn.micro.biz.entity.member;

import cn.micro.biz.commons.mybatis.entity.MicroEntity;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * Member Role Entity
 *
 * @author lry
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@TableName("member_role")
public class MemberRole extends MicroEntity<MemberRole> {

    private static final long serialVersionUID = 1L;

    /**
     * Member id
     * <p>
     * {@link Member#id}
     */
    private Long memberId;

    /**
     * Role id
     * <p>
     * {@link Role#id}
     */
    private Long roleId;

}
