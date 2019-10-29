package cn.micro.biz.service.action.impl;

import cn.micro.biz.entity.action.ActionLog;
import cn.micro.biz.type.action.ActionTypeEnum;

import cn.micro.biz.commons.mybatis.extension.MicroServiceImpl;
import cn.micro.biz.entity.action.Action;
import cn.micro.biz.mapper.action.ActionMapper;
import cn.micro.biz.model.like.OwnerAction;
import cn.micro.biz.model.like.TargetAction;
import cn.micro.biz.service.action.IActionService;
import org.springframework.stereotype.Service;

/**
 * Micro Like Service Implements
 *
 * @author lry
 */
@Service
public class ActionServiceImpl extends MicroServiceImpl<ActionMapper, Action> implements IActionService {

    @Override
    public boolean click(OwnerAction ownerAction, TargetAction targetAction) {
        return false;
    }

    /**
     * 点赞
     * <p>
     * 当用户未点赞时触发
     */
    private void submit(OwnerAction ownerAction, TargetAction targetAction) {
        Action action = new Action(ownerAction, targetAction);
        action.setStatus(ActionTypeEnum.LIKE.getValue());
    }

    /**
     * 取消点赞
     * <p>
     * 当用户已经点赞时触发
     */
    private void cancel(OwnerAction ownerAction, TargetAction targetAction) {
        Action action = new Action(ownerAction, targetAction);
        action.setStatus(ActionTypeEnum.CANCEL.getValue());
    }


    /**
     * 创建点赞日志
     */
    public void createLikeLog(OwnerAction ownerAction, TargetAction targetAction) {
        ActionLog actionLog = new ActionLog(ownerAction, targetAction);
        actionLog.setStatus(ActionTypeEnum.LIKE.getValue());
    }

    /**
     * 创建取消点赞日志
     */
    public void createCancelLog(OwnerAction ownerAction, TargetAction targetAction) {
        ActionLog actionLog = new ActionLog(ownerAction, targetAction);
        actionLog.setStatus(ActionTypeEnum.CANCEL.getValue());
    }

    /**
     * 获取点赞数量
     * <p>
     * 获取计数的高并发
     *
     * @return
     */
    public int targetCount(TargetAction targetAction) {
        return 0;
    }

    /**
     * 增加点赞数
     */
    public void incrTargetCount(TargetAction targetAction) {

    }

    /**
     * 减少点赞数
     * <p>
     * count不能小于零
     */
    public void decrTargetCount(TargetAction targetAction) {

    }

}
