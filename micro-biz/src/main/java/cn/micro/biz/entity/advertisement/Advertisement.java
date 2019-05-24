package cn.micro.biz.entity.advertisement;

import cn.micro.biz.commons.mybatis.entity.MicroEntity;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.*;

import java.util.Date;

/**
 * Advertisement Entity
 * <p>
 * http://www.wstmart.net/database-887.html
 * https://blog.csdn.net/juedaihuaihuai/article/details/4328292
 *
 * @author lry
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@TableName("advertisement")
public class Advertisement extends MicroEntity<Advertisement> {

    private static final long serialVersionUID = 1L;

    /**
     * Advertisement position id
     * <p>
     * {@link AdvertisementPosition#id}
     */
    private Long positionId;
    /**
     * AdvertisementStore id
     * <p>
     * {@link AdvertisementStore#id}
     */
    private Long storeId;

    /**
     * Advertisement status
     **/
    private Integer status;
    /**
     * Advertisement category
     */
    private Integer category;
    /**
     * Advertisement name
     */
    private Integer name;
    /**
     * Advertisement icon
     */
    private Integer icon;
    /**
     * Advertisement url
     */
    private Integer url;
    /**
     * Attributes intro
     **/
    private String intro;
    /**
     * Advertisement sort
     */
    private Integer sort;

    /**
     * Advertisement start time
     */
    private Date startTime;
    /**
     * Advertisement end time
     */
    private Date endTime;

    /**
     * Advertisement remark
     **/
    private String remark;
    
}
