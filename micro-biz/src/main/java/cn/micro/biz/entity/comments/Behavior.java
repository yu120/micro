package cn.micro.biz.entity.comments;

import cn.micro.biz.commons.mybatis.entity.MicroEntity;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.*;

/**
 * Behavior Entity
 *
 * @author lry
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@TableName("behavior")
public class Behavior extends MicroEntity<Behavior> {

    private static final long serialVersionUID = 1L;

    /**
     * Behavior origin category
     * <p>
     * {@link cn.micro.biz.type.comments.BehaviorOriginEnum}
     */
    private Integer origin;
    /**
     * Behavior origin id
     */
    private Long originId;

    /**
     * Behavior member id
     * <p>
     * {@link cn.micro.biz.entity.member.Member#id}
     **/
    private Long memberId;
    /**
     * Behavior category
     * <p>
     * {@link cn.micro.biz.type.comments.BehaviorCategoryEnum}
     **/
    private Integer category;

}
