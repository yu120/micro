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
     * Member id
     */
    private Long memberId;
    /**
     * IP address
     */
    private String ip;
    /**
     * 全局ID(UUID填充)
     */
    private String unionId;
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
     * Start time
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date startTime;
    /**
     * Expire second
     */
    private Integer expire;

}
