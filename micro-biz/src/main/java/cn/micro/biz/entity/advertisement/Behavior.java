package cn.micro.biz.entity.advertisement;

import cn.micro.biz.commons.mybatis.MicroEntity;
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
public class Behavior extends MicroEntity<Behavior> {

    private static final long serialVersionUID = 1L;

    /**
     * Behavior category
     **/
    private BehaviorCategoryEnum category;
    /**
     * Behavior origin id
     */
    private Long originId;
    /**
     * Behavior member id
     *
     * @see cn.micro.biz.entity.member.Member#id
     **/
    private Long memberId;

}
