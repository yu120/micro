package cn.micro.biz.service.action;

import cn.micro.biz.commons.mybatis.extension.IMicroService;
import cn.micro.biz.entity.action.Action;
import cn.micro.biz.model.like.OwnerAction;
import cn.micro.biz.model.like.TargetAction;

/**
 * 点赞是互联网中常见的交互方式，系统根据用户的点赞操作来跟踪用户的行为，并对用户的喜好进行分析。
 * <p>
 * 计数功能设计: 最简单的计数功能，便是通过 SQL 对 Like 进行 “count group by” 来完成，但在高并发系统中，group by 是一大忌讳。
 * <p>
 * 非功能性需求需要考虑:
 * 1.点击行为的高并发
 * 2.获取计数的高并发
 * 3.Like 与 Logger、 Count 的数据一致性
 *
 * @author lry
 */
public interface IActionService extends IMicroService<Action> {

    /**
     * 用户点击行为
     * <p>
     * 点击行为的高并发
     *
     * @param ownerAction  {@link OwnerAction}
     * @param targetAction {@link TargetAction}
     * @return true is success
     */
    boolean operate(OwnerAction ownerAction, TargetAction targetAction);

}
