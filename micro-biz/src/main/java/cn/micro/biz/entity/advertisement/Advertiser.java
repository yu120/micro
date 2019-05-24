package cn.micro.biz.entity.advertisement;

import cn.micro.biz.commons.mybatis.entity.MicroEntity;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.*;

/**
 * Advertiser Entity
 *
 * @author lry
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@TableName("advertiser")
public class Advertiser extends MicroEntity<Advertiser> {

    private static final long serialVersionUID = 1L;

    /**
     * Advertiser name
     */
    private String name;
    /**
     * Advertiser company
     */
    private String company;
    /**
     * Advertiser tel
     */
    private String tel;
    /**
     * Advertiser qq number
     */
    private String qq;
    /**
     * Advertiser wx number
     */
    private String wx;
    /**
     * Advertiser address
     */
    private String address;
    /**
     * Advertiser level category
     */
    private Integer category;
    
}
