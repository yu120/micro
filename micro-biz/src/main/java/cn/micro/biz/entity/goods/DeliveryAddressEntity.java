package cn.micro.biz.entity.goods;

import cn.micro.biz.commons.mybatis.MicroEntity;
import cn.micro.biz.entity.member.MemberEntity;
import cn.micro.biz.entity.unified.AreaEntity;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.*;

/**
 * Delivery Address Entity
 *
 * @author lry
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@TableName("delivery_address")
public class DeliveryAddressEntity extends MicroEntity<DeliveryAddressEntity> {

    /**
     * Member id
     *
     * @see MemberEntity#id
     **/
    private Long memberId;

    /**
     * Delivery member name
     **/
    private String name;
    /**
     * Tel
     **/
    private String tel;
    /**
     * Backup tel
     **/
    private String telBackup;

    /**
     * Zip code
     **/
    private String zip;
    /**
     * Default delivery address
     **/
    private Integer defaultAddress;

    /**
     * Country code
     *
     * @see AreaEntity#code
     **/
    private String countryCode;
    /**
     * Country name
     *
     * @see AreaEntity#name
     **/
    private String countryName;
    /**
     * Province code
     *
     * @see AreaEntity#code
     **/
    private String provinceCode;
    /**
     * Province name
     *
     * @see AreaEntity#name
     **/
    private String provinceName;
    /**
     * City code
     *
     * @see AreaEntity#code
     **/
    private String cityCode;
    /**
     * City name
     *
     * @see AreaEntity#name
     **/
    private String cityName;
    /**
     * District code
     *
     * @see AreaEntity#code
     **/
    private String districtCode;
    /**
     * District name
     *
     * @see AreaEntity#name
     **/
    private String districtName;
    /**
     * Address
     **/
    private String address;

}
