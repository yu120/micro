package cn.micro.biz.entity.member;

import cn.micro.biz.commons.mybatis.entity.MicroEntity;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * Permission Entity
 *
 * @author lry
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@TableName("permission")
public class Permission extends MicroEntity<Permission> {

    private static final long serialVersionUID = 1L;

    /**
     * Parent permission id
     * <p>
     * {@link Permission#id}
     */
    private Long parentId;

    /**
     * Permission code
     */
    private String code;
    /**
     * Permission name
     */
    private String name;
    /**
     * Permission intro
     */
    private String intro;

    /**
     * Permission category. eg: page,menu,link etc.
     */
    private Integer category;
    /**
     * Permission uri
     */
    private String uri;

}
