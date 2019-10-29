package cn.micro.biz.model.like;

import lombok.Data;

import java.io.Serializable;

@Data
public class TargetAction implements Serializable {

    /**
     * 点赞目标ID
     */
    private Long targetId;
    /**
     * 点赞目标类型
     * <p>
     * {@link cn.micro.biz.type.like.TargetTypeEnum}
     */
    private Integer targetType;

}
