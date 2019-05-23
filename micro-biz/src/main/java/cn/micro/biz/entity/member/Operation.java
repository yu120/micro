package cn.micro.biz.entity.member;

import cn.micro.biz.commons.mybatis.entity.MicroEntity;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.*;

/**
 * Operation Entity
 *
 * @author lry
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@TableName("operation")
public class Operation extends MicroEntity<Operation> {

    private static final long serialVersionUID = 1L;

    /**
     * Parent operation id
     * <p>
     * {@link Permission#id}
     */
    private Long parentId;

    /**
     * Operation name
     */
    private String name;
    /**
     * Operation code
     */
    private String code;
    /**
     * Operation enable status
     */
    private Integer status;
    /**
     * Operation intercept url matching rule
     */
    private String urlMatching;

}
