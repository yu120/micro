package cn.micro.biz.entity.advertisement;

import cn.micro.biz.commons.mybatis.MicroEntity;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.*;

import java.util.Date;

/**
 * Advertisement Entity
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
@TableName("advertisement")
public class AdvertisementEntity extends MicroEntity<AdvertisementEntity> {

    private static final long serialVersionUID = 1L;

    /**
     * Advertisement position id
     *
     * @see AdvertisementPositionEntity#id
     */
    private Long positionId;
    /**
     * AdvertisementStore id
     *
     * @see AdvertisementStoreEntity#id
     */
    private Long storeId;

    /**
     * Advertisement status
     **/
    private Integer status;
    /**
     * Advertisement category
     */
    private Integer category;
    /**
     * Advertisement name
     */
    private String name;
    /**
     * Advertisement icon
     */
    private String icon;
    /**
     * Advertisement url
     */
    private String url;
    /**
     * Attributes intro
     **/
    private String intro;
    /**
     * Advertisement sort
     */
    private Integer sort;

    /**
     * Advertisement start time
     */
    private Date startTime;
    /**
     * Advertisement end time
     */
    private Date endTime;

    /**
     * Advertisement remark
     **/
    private String remark;

}
