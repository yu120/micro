package cn.micro.biz.entity.advertisement;

import cn.micro.biz.commons.mybatis.entity.MicroEntity;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.*;

import java.util.Date;

/**
 * Advertisement
 * <p>
 * http://www.wstmart.net/database-887.html
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

    private Long positionId;
    private Date startTime;
    private Date endTime;
    private Integer sort;
    private Integer clickNum;

    private Integer category;
    private Integer name;
    private Integer icon;
    private Integer url;

}
