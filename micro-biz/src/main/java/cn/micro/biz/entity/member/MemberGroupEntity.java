package cn.micro.biz.entity.member;

import cn.micro.biz.commons.mybatis.MicroEntity;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.*;

/**
 * Member Group Entity
 *
 * @author lry
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@TableName("member_group")
public class MemberGroupEntity extends MicroEntity<MemberGroupEntity> {

    private static final long serialVersionUID = 1L;

    /**
     * Parent member group id
     *
     * @see MemberGroupEntity#id
     */
    private Long parentId;

    /**
     * Member group code
     */
    private String code;
    /**
     * Member group name
     */
    private String name;
    /**
     * Member group intro
     */
    private String intro;

}
