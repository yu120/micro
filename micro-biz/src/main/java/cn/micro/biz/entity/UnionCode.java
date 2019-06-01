package cn.micro.biz.entity;

import cn.micro.biz.commons.mybatis.entity.MicroEntity;
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
     * 全局ID(UUID填充)
     */
    private String account;
    /**
     * Union code category
     * <p>
     * {@link cn.micro.biz.type.UnionCodeCategoryEnum}
     */
    private Integer category;
    /**
     * Union code value
     */
    private String code;
    /**
     * 最大验证次数
     */
    private Integer maxTimes;
    /**
     * 失败次数
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
