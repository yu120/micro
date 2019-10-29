package cn.micro.biz.model.like;

import lombok.Data;

import java.io.Serializable;

@Data
public class OwnerAction implements Serializable {

    /**
     * 点赞发起者ID
     */
    private Long ownerId;
    /**
     * 点赞发起者类型
     * <p>
     * {@link cn.micro.biz.type.action.OwnerTypeEnum}
     */
    private Integer ownerType;

}
