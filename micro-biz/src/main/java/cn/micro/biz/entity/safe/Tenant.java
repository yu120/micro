package cn.micro.biz.entity.safe;

import cn.micro.biz.commons.mybatis.MicroEntity;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.*;

import java.sql.Date;

/**
 * Tenant Entity
 *
 * @author lry
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@TableName("tenant")
public class Tenant extends MicroEntity<Tenant> {

    private static final long serialVersionUID = 1L;

    /**
     * Tenant enable status
     */
    private Integer status;
    /**
     * Tenant name
     */
    private String name;
    /**
     * Tenant intro
     */
    private String intro;
    /**
     * Start date
     */
    private Date startDate;
    /**
     * End date
     */
    private Date endDate;

}
