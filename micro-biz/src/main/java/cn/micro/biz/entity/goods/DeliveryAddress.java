package cn.micro.biz.entity.goods;

import cn.micro.biz.commons.mybatis.entity.MicroEntity;
import cn.micro.biz.entity.member.Member;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.*;

/**
 * 收货地址
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
     * 用户ID
     * {@link Member#id}
     **/
    private Long memberId;

    /**
     * 收件人姓名
     **/
    private String name;
    /**
     * 联系电话
     **/
    private String tel;
    /**
     * 备用联系电话
     **/
    private String telBackup;

    /**
     * 邮政编码
     **/
    private String zip;
    /**
     * 是否默认收货地址
     **/
    private Integer defaultAddress;

    /**
     * 国家
     **/
    private String country;
    /**
     * 省份
     **/
    private String province;
    /**
     * 城市
     **/
    private String city;
    /**
     * 地区
     **/
    private String area;
    /**
     * 街道/详细收货地址
     **/
    private String street;

}
