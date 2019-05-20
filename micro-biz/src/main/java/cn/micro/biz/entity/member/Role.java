package cn.micro.biz.entity.member;

import cn.micro.biz.commons.mybatis.entity.MicroEntity;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * Role Entity
 *
 * @author lry
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@TableName("role")
public class Role extends MicroEntity<Role> {

    private static final long serialVersionUID = 1L;

    /**
     * Parent role id
     * <p>
     * {@link Role#id}
     */
    private Long parentId;

    /**
     * Role code
     */
    private String code;
    /**
     * Role name
     */
    private String name;
    /**
     * Role intro
     */
    private String intro;

}
