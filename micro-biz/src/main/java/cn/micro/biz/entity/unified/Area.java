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
     * <p>
     * {@link Area#code}
     */
    private String parentCode;
    /**
     * Parent area id
     * <p>
     * {@link Area#id}
     */
    private Long parentId;

    /**
     * Province code
     * <p>
     * {@link Area#code}
     */
    private String provinceCode;
    /**
     * Province name
     * <p>
     * {@link Area#name}
     */
    private String provinceName;
    /**
     * City code
     * <p>
     * {@link Area#code}
     */
    private String cityCode;
    /**
     * City name
     * <p>
     * {@link Area#name}
     */
    private String cityName;
    /**
     * District code
     * <p>
     * {@link Area#code}
     */
    private String districtCode;
    /**
     * District name
     * <p>
     * {@link Area#name}
     */
    private String districtName;
    /**
     * Town code
     * <p>
     * {@link Area#code}
     */
    private String townCode;
    /**
     * Town name
     * <p>
     * {@link Area#name}
     */
    private String townName;

}
