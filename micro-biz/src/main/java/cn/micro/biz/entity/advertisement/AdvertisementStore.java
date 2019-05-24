package cn.micro.biz.entity.advertisement;

import cn.micro.biz.commons.mybatis.entity.MicroEntity;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.*;

/**
 * Advertisement Store Entity
 *
 * @author lry
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@TableName("advertisement_store")
public class AdvertisementStore extends MicroEntity<AdvertisementStore> {

    private static final long serialVersionUID = 1L;

    /**
     * Advertisement store name
     */
    private String name;
    /**
     * Advertisement store company
     */
    private String company;
    /**
     * Advertisement store tel
     */
    private String tel;
    /**
     * Advertisement store qq number
     */
    private String qq;
    /**
     * Advertisement store wx number
     */
    private String wx;
    /**
     * Advertisement store address
     */
    private String address;
    /**
     * Advertisement store level category
     */
    private Integer category;
    
}
