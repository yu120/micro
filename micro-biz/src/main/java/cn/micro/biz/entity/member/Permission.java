package cn.micro.biz.entity.member;

import cn.micro.biz.commons.mybatis.MicroEntity;
import cn.micro.biz.type.member.PermissionCategoryEnum;
import cn.micro.biz.type.member.StatusEnum;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.*;

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
     *
     * @see Permission#id
     */
    private Long parentId;
    /**
     * Application id
     *
     * @see cn.micro.biz.entity.unified.App#id
     */
    private Long appId;

    /**
     * Permission category
     * <p>
     * eg: tab,menu,element,link etc.
     */
    private PermissionCategoryEnum category;
    /**
     * Permission enable status
     */
    private StatusEnum status;
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
