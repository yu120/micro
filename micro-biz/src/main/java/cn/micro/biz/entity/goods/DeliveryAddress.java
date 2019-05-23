package cn.micro.biz.entity.goods;

import cn.micro.biz.commons.mybatis.entity.MicroEntity;
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
     * <p>
     * {@link cn.micro.biz.entity.member.Member#id}
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
     * <p>
     * {@link cn.micro.biz.entity.member.Area#code}
     **/
    private String countryCode;
    /**
     * Country name
     * <p>
     * {@link cn.micro.biz.entity.member.Area#name}
     **/
    private String countryName;
    /**
     * Province code
     * <p>
     * {@link cn.micro.biz.entity.member.Area#code}
     **/
    private String provinceCode;
    /**
     * Province name
     * <p>
     * {@link cn.micro.biz.entity.member.Area#name}
     **/
    private String provinceName;
    /**
     * City code
     * <p>
     * {@link cn.micro.biz.entity.member.Area#code}
     **/
    private String cityCode;
    /**
     * City name
     * <p>
     * {@link cn.micro.biz.entity.member.Area#name}
     **/
    private String cityName;
    /**
     * District code
     * <p>
     * {@link cn.micro.biz.entity.member.Area#code}
     **/
    private String districtCode;
    /**
     * District name
     * <p>
     * {@link cn.micro.biz.entity.member.Area#name}
     **/
    private String districtName;
    /**
     * Address
     **/
    private String address;

}
