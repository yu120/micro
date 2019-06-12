package cn.micro.biz.entity.goods;

import cn.micro.biz.commons.mybatis.MicroEntity;
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
public class DeliveryAddress extends MicroEntity<DeliveryAddress> {

    /**
     * Member id
     *
     * @see cn.micro.biz.entity.member.Member#id
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
     * @see cn.micro.biz.entity.unified.Area#code
     **/
    private String countryCode;
    /**
     * Country name
     *
     * @see cn.micro.biz.entity.unified.Area#name
     **/
    private String countryName;
    /**
     * Province code
     *
     * @see cn.micro.biz.entity.unified.Area#code
     **/
    private String provinceCode;
    /**
     * Province name
     *
     * @see cn.micro.biz.entity.unified.Area#name
     **/
    private String provinceName;
    /**
     * City code
     *
     * @see cn.micro.biz.entity.unified.Area#code
     **/
    private String cityCode;
    /**
     * City name
     *
     * @see cn.micro.biz.entity.unified.Area#name
     **/
    private String cityName;
    /**
     * District code
     *
     * @see cn.micro.biz.entity.unified.Area#code
     **/
    private String districtCode;
    /**
     * District name
     *
     * @see cn.micro.biz.entity.unified.Area#name
     **/
    private String districtName;
    /**
     * Address
     **/
    private String address;

}
