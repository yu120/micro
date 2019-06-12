package cn.micro.biz.entity.unified;

import cn.micro.biz.commons.mybatis.MicroEntity;
import cn.micro.biz.type.unified.UnionCodeCategoryEnum;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;

import java.util.Date;

/**
 * Union Code Entity
 *
 * @author lry
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@TableName("union_code")
public class UnionCode extends MicroEntity<UnionCode> {

    private static final long serialVersionUID = 1L;

    /**
     * Global ID(UUID fill)
     */
    private String account;
    /**
     * Union code category
     */
    private UnionCodeCategoryEnum category;
    /**
     * Union code value
     */
    private String code;
    /**
     * Max check times
     */
    private Integer maxTimes;
    /**
     * Max fail times
     */
    private Integer failTimes;
    /**
     * Start time
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date startTime;
    /**
     * Expire second
     */
    private Integer expire;

}
