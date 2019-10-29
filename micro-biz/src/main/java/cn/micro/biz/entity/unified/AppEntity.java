package cn.micro.biz.entity.unified;

import cn.micro.biz.commons.mybatis.MicroEntity;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.*;

/**
 * Application Entity
 *
 * @author lry
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@TableName("app")
public class AppEntity extends MicroEntity<AppEntity> {

    private static final long serialVersionUID = 1L;

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
