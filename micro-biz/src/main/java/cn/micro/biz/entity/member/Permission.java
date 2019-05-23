package cn.micro.biz.entity.member;

import cn.micro.biz.commons.mybatis.entity.MicroEntity;
import cn.micro.biz.type.member.PermissionCategoryEnum;
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
     * Application id
     * <p>
     * {@link App#id}
     */
    private Long appId;

    /**
     * Permission category. eg: tab,menu,element,link etc.
     * <p>
     * {@link PermissionCategoryEnum}
     */
    private Integer category;
    /**
     * Permission enable status
     */
    private Integer status;
    /**
     * Permission code
     */
    private String code;
    /**
     * Permission name
     */
    private String name;
    /**
     * Permission icon
     */
    private String icon;
    /**
     * Permission url
     */
    private String url;
    /**
     * Permission intro
     */
    private String intro;

}
