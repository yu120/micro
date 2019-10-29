package cn.micro.biz.entity.unified;

import cn.micro.biz.commons.mybatis.MicroEntity;
import cn.micro.biz.entity.member.MemberEntity;
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
public class AuditRecordEntity extends MicroEntity<AuditRecordEntity> {

    private static final long serialVersionUID = 1L;

    /**
     * Audit id
     *
     * @see AuditEntity#id
     */
    private Long auditId;
    /**
     * Audit member id
     *
     * @see MemberEntity#id
     */
    private Long memberId;

    /**
     * Audit content
     */
    private String content;

}
