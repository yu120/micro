package cn.micro.biz.entity.advertisement;

import cn.micro.biz.commons.mybatis.MicroEntity;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.*;

/**
 * Advertisement Position Entity
 *
 * @author lry
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@TableName("advertisement_position")
public class AdvertisementPositionEntity extends MicroEntity<AdvertisementPositionEntity> {

    private static final long serialVersionUID = 1L;

    /**
     * Advertisement enable status
     */
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
     * Advertisement code
     */
    private String code;
    /**
     * Advertisement width
     */
    private Integer width;
    /**
     * Advertisement height
     */
    private Integer height;
    /**
     * Advertisement image max size
     */
    private Integer maxSize;
    /**
     * Advertisement rotation time(ms)
     */
    private Integer rotationTime;

}
