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
public class Area extends MicroEntity<Area> {

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
     * @see Area#code
     */
    private String parentCode;
    /**
     * Parent area id
     *
     * @see Area#id
     */
    private Long parentId;

    /**
     * Province code
     *
     * @see Area#code
     */
    private String provinceCode;
    /**
     * Province name
     *
     * @see Area#name
     */
    private String provinceName;
    /**
     * City code
     *
     * @see Area#code
     */
    private String cityCode;
    /**
     * City name
     *
     * @see Area#name
     */
    private String cityName;
    /**
     * District code
     *
     * @see Area#code
     */
    private String districtCode;
    /**
     * District name
     *
     * @see Area#name
     */
    private String districtName;
    /**
     * Town code
     *
     * @see Area#code
     */
    private String townCode;
    /**
     * Town name
     *
     * @see Area#name
     */
    private String townName;

}
