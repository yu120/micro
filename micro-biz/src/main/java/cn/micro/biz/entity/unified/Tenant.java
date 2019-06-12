package cn.micro.biz.entity.unified;

import cn.micro.biz.commons.mybatis.MicroEntity;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;

import java.util.Date;

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
    @JsonFormat(pattern = "yyyy-MM-dd 00:00:00")
    private Date startDate;
    /**
     * End date
     */
    @JsonFormat(pattern = "yyyy-MM-dd 23:59:59")
    private Date endDate;

}
