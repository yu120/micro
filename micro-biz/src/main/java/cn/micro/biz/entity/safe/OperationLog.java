package cn.micro.biz.entity.safe;

import cn.micro.biz.commons.mybatis.entity.MicroEntity;
import cn.micro.biz.entity.member.Member;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.*;

/**
 * Operation Log Entity
 *
 * @author lry
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@TableName("operation_log")
public class OperationLog extends MicroEntity<OperationLog> {

    private static final long serialVersionUID = 1L;

    /**
     * Operation id
     * <p>
     * {@link Operation#id}
     */
    private Long operationId;
    /**
     * Operation member id
     * <p>
     * {@link Member#id}
     */
    private Long memberId;

    /**
     * Operation content
     */
    private String content;

}
