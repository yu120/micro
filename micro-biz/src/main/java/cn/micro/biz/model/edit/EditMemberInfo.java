package cn.micro.biz.model.edit;

import cn.micro.biz.entity.unified.AreaEntity;
import cn.micro.biz.type.member.SexEnum;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.io.Serializable;
import java.util.Date;

/**
 * Edit Member Info
 *
 * @author lry
 */
@Data
@ToString
@EqualsAndHashCode
public class EditMemberInfo implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * Member name
     */
    private String name;
    /**
     * Member head image icon url
     */
    private String icon;
    /**
     * Member sex
     * <p>
     * {@link SexEnum}
     */
    private SexEnum sex;
    /**
     * Age
     */
    private Integer age;
    /**
     * Id card
     */
    private String idCard;
    /**
     * Member birth
     */
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date birth;
    /**
     * Member address
     */
    private String address;

    /**
     * Province code
     * <p>
     * {@link AreaEntity#code}
     */
    private String provinceCode;
    /**
     * Province name
     * <p>
     * {@link AreaEntity#name}
     */
    private String provinceName;
    /**
     * City code
     * <p>
     * {@link AreaEntity#code}
     */
    private String cityCode;
    /**
     * City name
     * <p>
     * {@link AreaEntity#name}
     */
    private String cityName;
    /**
     * District code
     * <p>
     * {@link AreaEntity#code}
     */
    private String districtCode;
    /**
     * District name
     * <p>
     * {@link AreaEntity#name}
     */
    private String districtName;
    /**
     * Town code
     * <p>
     * {@link AreaEntity#code}
     */
    private String townCode;
    /**
     * Town name
     * <p>
     * {@link AreaEntity#name}
     */
    private String townName;

}
