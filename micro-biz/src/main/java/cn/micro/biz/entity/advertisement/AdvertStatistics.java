package cn.micro.biz.entity.advertisement;

import cn.micro.biz.commons.mybatis.entity.MicroEntity;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.*;

/**
 * Advert Statistics Entity
 *
 * @author lry
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@TableName("advert_statistics")
public class AdvertStatistics extends MicroEntity<AdvertStatistics> {

    private static final long serialVersionUID = 1L;

    /**
     * Advert category
     */
    private Integer category;
    /**
     * Advert enable status
     */
    private Integer status;
    /**
     * Advert name
     */
    private String name;
    /**
     * Advert code
     */
    private String code;
    /**
     * Advert width
     */
    private Integer width;
    /**
     * Advert height
     */
    private Integer height;

}
