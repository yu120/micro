package cn.micro.biz.entity.advertisement;

import cn.micro.biz.commons.mybatis.entity.MicroEntity;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.*;

import java.util.Date;

/**
 * Advert Entity
 * <p>
 * http://www.wstmart.net/database-887.html
 * https://blog.csdn.net/juedaihuaihuai/article/details/4328292
 *
 * @author lry
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@TableName("advert")
public class Advert extends MicroEntity<Advert> {

    private static final long serialVersionUID = 1L;

    /**
     * Advert position id
     * <p>
     * {@link AdvertPosition#id}
     */
    private Long advertPositionId;
    /**
     * Advertiser id
     * <p>
     * {@link Advertiser#id}
     */
    private Long advertiserId;

    /**
     * Advert category
     */
    private Integer category;
    /**
     * Advert name
     */
    private Integer name;
    /**
     * Advert icon
     */
    private Integer icon;
    /**
     * Advert url
     */
    private Integer url;
    /**
     * Attributes intro
     **/
    private String intro;
    /**
     * Advert sort
     */
    private Integer sort;

    /**
     * Advert start time
     */
    private Date startTime;
    /**
     * Advert end time
     */
    private Date endTime;

}
