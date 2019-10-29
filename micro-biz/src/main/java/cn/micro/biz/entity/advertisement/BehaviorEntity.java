package cn.micro.biz.entity.advertisement;

import cn.micro.biz.commons.mybatis.MicroEntity;
import cn.micro.biz.entity.member.MemberEntity;
import cn.micro.biz.type.advertisement.BehaviorCategoryEnum;
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
public class BehaviorEntity extends MicroEntity<BehaviorEntity> {

    private static final long serialVersionUID = 1L;

    /**
     * Behavior category
     *
     * @serial tinyint(3)
     **/
    private BehaviorCategoryEnum category;
    /**
     * Behavior origin id
     */
    private Long originId;
    /**
     * Behavior member id
     *
     * @see MemberEntity#id
     **/
    private Long memberId;

}
