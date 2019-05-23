package cn.micro.biz.entity.advertisement;

import cn.micro.biz.commons.mybatis.entity.MicroEntity;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.*;

/**
 * Advertisement Position
 *
 * @author lry
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@TableName("advertisement_position")
public class AdvertisementPosition extends MicroEntity<AdvertisementPosition> {

    private static final long serialVersionUID = 1L;

    /**
     * Advertisement category
     */
    private Integer category;
    /**
     * Advertisement enable status
     */
    private Integer status;
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

}
