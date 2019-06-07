package cn.micro.biz.entity.safe;

import cn.micro.biz.commons.mybatis.MicroEntity;
import cn.micro.biz.entity.member.Permission;
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
public class Audit extends MicroEntity<Audit> {

    private static final long serialVersionUID = 1L;

    /**
     * Parent operation id
     * <p>
     * {@link Permission#id}
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
