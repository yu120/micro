package cn.micro.biz.entity.safe;

import cn.micro.biz.commons.mybatis.MicroEntity;
import cn.micro.biz.entity.member.Member;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.*;

/**
 * Audit Log Entity
 *
 * @author lry
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@TableName("audit_log")
public class AuditLog extends MicroEntity<AuditLog> {

    private static final long serialVersionUID = 1L;

    /**
     * Audit id
     * <p>
     * {@link Audit#id}
     */
    private Long operationId;
    /**
     * Audit member id
     * <p>
     * {@link Member#id}
     */
    private Long memberId;

    /**
     * Audit content
     */
    private String content;

}
