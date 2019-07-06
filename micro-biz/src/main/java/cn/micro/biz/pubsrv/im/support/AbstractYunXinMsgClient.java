package cn.micro.biz.pubsrv.im.support;

import cn.micro.biz.pubsrv.im.YunXinActionEnum;
import cn.micro.biz.pubsrv.im.model.msg.YunXinMsgBroadcastMsg;
import cn.micro.biz.pubsrv.im.model.msg.YunXinMsgRecall;
import cn.micro.biz.pubsrv.im.model.msg.YunXinMsgSendAttachMsg;
import cn.micro.biz.pubsrv.im.model.msg.YunXinMsgSendBatchAttachMsg;
import com.alibaba.fastjson.JSONObject;

/**
 * YunXin Msg
 *
 * @author lry
 */
public abstract class AbstractYunXinMsgClient extends AbstractYunXinHistoryClient {

    /**
     * 发送自定义系统通知
     * <p>
     * 接口描述:
     * 1.自定义系统通知区别于普通消息，方便开发者进行业务逻辑的通知；
     * 2.目前支持两种类型：点对点类型和群类型（仅限高级群），根据msgType有所区别。
     * <p>
     * 应用场景：如某个用户给另一个用户发送好友请求信息等，具体attach为请求消息体，第三方可以自行扩展，建议是json格式
     *
     * @param yunXinMsgSendAttachMsg {@link YunXinMsgSendAttachMsg}
     * @return success return true
     */
    public boolean msgSendAttachMsg(YunXinMsgSendAttachMsg yunXinMsgSendAttachMsg) {
        JSONObject jsonObject = super.request(YunXinActionEnum.MSG_SEND_ATTACH_MSG, yunXinMsgSendAttachMsg);
        return jsonObject != null;
    }

    /**
     * 批量发送点对点自定义系统通知
     * <p>
     * 接口描述:
     * 1.系统通知区别于普通消息，应用接收到直接交给上层处理，客户端可不做展示；
     * 2.目前支持类型：点对点类型；
     * 3.最大限500人，只能针对个人,如果批量提供的帐号中有未注册的帐号，会提示并返回给用户；
     * 4.此接口受频率控制，一个应用一分钟最多调用120次，超过会返回416状态码，并且被屏蔽一段时间；
     * <p>
     * 应用场景：如某个用户给另一个用户发送好友请求信息等，具体attach为请求消息体，第三方可以自行扩展，建议是json格式
     *
     * @param yunXinMsgSendBatchAttachMsg {@link YunXinMsgSendBatchAttachMsg}
     * @return success return true
     */
    public boolean msgSendBatchAttachMsg(YunXinMsgSendBatchAttachMsg yunXinMsgSendBatchAttachMsg) {
        JSONObject jsonObject = super.request(YunXinActionEnum.MSG_SEND_BATCH_ATTACH_MSG, yunXinMsgSendBatchAttachMsg);
        return jsonObject != null;
    }

    /**
     * 消息撤回
     * <p>
     * 接口描述:
     * 消息撤回接口，可以撤回一定时间内的点对点与群消息
     *
     * @param yunXinMsgRecall {@link YunXinMsgRecall}
     * @return success return true
     */
    public boolean msgRecall(YunXinMsgRecall yunXinMsgRecall) {
        JSONObject jsonObject = super.request(YunXinActionEnum.MSG_RECALL, yunXinMsgRecall);
        return jsonObject != null;
    }

    /**
     * 发送广播消息
     * <p>
     * 接口描述:
     * 1、广播消息，可以对应用内的所有用户发送广播消息，广播消息目前暂不支持第三方推送（APNS、小米、华为等）；
     * 2、广播消息支持离线存储，并可以自定义设置离线存储的有效期，最多保留最近100条离线广播消息；
     * 3、此接口受频率控制，一个应用一分钟最多调用10次，一天最多调用1000次，超过会返回416状态码；
     * 4、该功能目前需申请开通，详情可咨询您的客户经理。
     *
     * @param yunXinMsgBroadcastMsg {@link YunXinMsgBroadcastMsg}
     * @return success return true
     */
    public boolean msgBroadcastMsg(YunXinMsgBroadcastMsg yunXinMsgBroadcastMsg) {
        JSONObject jsonObject = super.request(YunXinActionEnum.MSG_BROADCAST_MSG, yunXinMsgBroadcastMsg);
        return jsonObject != null;
    }

}
