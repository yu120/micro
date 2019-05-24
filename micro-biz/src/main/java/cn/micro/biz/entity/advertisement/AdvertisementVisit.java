package cn.micro.biz.entity.advertisement;

import cn.micro.biz.commons.mybatis.entity.MicroEntity;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.*;

/**
 * Advertisement Visit Entity
 *
 * @author lry
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@TableName("advertisement_visit")
public class AdvertisementVisit extends MicroEntity<AdvertisementVisit> {

    private static final long serialVersionUID = 1L;

    /**
     * Advertisement id
     * <p>
     * {@link Advertisement#id}
     */
    private Long advertisementId;

    /**
     * Visitor ip
     */
    private String ip;
    /**
     * Visitor local language
     */
    private String language;
    /**
     * Visitor operating system
     */
    private String os;

    /**
     * Visitor browser kernel
     */
    private String browserKernel;
    /**
     * Visitor browser category
     */
    private String browserCategory;
    /**
     * Visitor browser version
     */
    private String browserVersion;

    /**
     * Visitor local has flash
     */
    private Integer flash;
    /**
     * Visitor local flash version
     */
    private String flashVersion;

    /**
     * Geographical position type(高德/百度/谷歌)
     */
    private Integer geographical;
    /**
     * Visitor geographical position longitude
     */
    private String longitude;
    /**
     * Visitor geographical position latitude
     */
    private String latitude;

}
