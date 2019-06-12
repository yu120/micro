package cn.micro.biz.entity.unified;

import cn.micro.biz.commons.mybatis.MicroEntity;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.*;

/**
 * Audit Record Entity
 *
 * @author lry
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@TableName("audit_record")
public class AuditRecord extends MicroEntity<AuditRecord> {

    private static final long serialVersionUID = 1L;

    /**
     * Audit id
     *
     * @see Audit#id
     */
    private Long auditId;
    /**
     * Audit member id
     *
     * @see cn.micro.biz.entity.member.Member#id
     */
    private Long memberId;

    /**
     * Audit content
     */
    private String content;

}
