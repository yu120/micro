package cn.micro.biz.entity.unified;

import cn.micro.biz.commons.mybatis.MicroEntity;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.*;

/**
 * Area Entity
 *
 * @author lry
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@TableName("area")
public class AreaEntity extends MicroEntity<AreaEntity> {

    private static final long serialVersionUID = 1L;

    /**
     * Area code
     */
    private String code;
    /**
     * Area name
     */
    private String name;
    /**
     * Area level type
     */
    private Integer levelType;

    /**
     * Parent area code
     *
     * @see AreaEntity#code
     */
    private String parentCode;
    /**
     * Country area id
     *
     * @see AreaEntity#id
     */
    private Long parentId;

    /**
     * Province code
     *
     * @see AreaEntity#code
     */
    private String provinceCode;
    /**
     * Province name
     *
     * @see AreaEntity#name
     */
    private String provinceName;

    /**
     * City code
     *
     * @see AreaEntity#code
     */
    private String cityCode;
    /**
     * City name
     *
     * @see AreaEntity#name
     */
    private String cityName;

    /**
     * District code
     *
     * @see AreaEntity#code
     */
    private String districtCode;
    /**
     * District name
     *
     * @see AreaEntity#name
     */
    private String districtName;

    /**
     * Town code
     *
     * @see AreaEntity#code
     */
    private String townCode;
    /**
     * Town name
     *
     * @see AreaEntity#name
     */
    private String townName;

}
