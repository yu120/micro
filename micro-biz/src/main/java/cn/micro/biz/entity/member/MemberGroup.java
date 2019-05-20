package cn.micro.biz.entity.member;

import cn.micro.biz.commons.mybatis.entity.MicroEntity;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.*;

/**
 * Member Group
 *
 * @author lry
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@TableName("member_group")
public class MemberGroup extends MicroEntity<MemberGroup> {

    private static final long serialVersionUID = 1L;

    /**
     * {@link MemberGroup#id}
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
