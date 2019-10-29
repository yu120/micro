package cn.micro.biz.entity.unified;

import cn.micro.biz.commons.mybatis.MicroEntity;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.*;

/**
 * Audit Entity
 *
 * @author lry
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@TableName("audit")
public class AuditEntity extends MicroEntity<AuditEntity> {

    private static final long serialVersionUID = 1L;

    /**
     * Parent operation id
     *
     * @see AuditEntity#id
     */
    private Long parentId;

    /**
     * Audit name
     */
    private String name;
    /**
     * Audit code
     */
    private String code;
    /**
     * Audit enable status
     */
    private Integer status;
    /**
     * Audit intercept url matching rule
     */
    private String urlMatching;

}
