package cn.micro.biz.pubsrv.im.support;

import cn.micro.biz.pubsrv.im.YunXinActionEnum;
import cn.micro.biz.pubsrv.im.model.friend.*;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import java.util.Collections;
import java.util.List;

/**
 * 用户关系托管
 *
 * @author lry
 */
public abstract class AbstractYunXinFriendClient extends AbstractYunXinChatRoomClient {

    /**
     * 加好友
     * <p>
     * 注意：两人保持好友关系
     *
     * @param yunXinFriendAdd {@link YunXinFriendAdd}
     * @return success return true
     */
    public boolean friendAdd(YunXinFriendAdd yunXinFriendAdd) {
        JSONObject jsonObject = super.request(YunXinActionEnum.FRIEND_ADD, yunXinFriendAdd);
        return jsonObject != null;
    }

    /**
     * 更新好友相关信息
     * <p>
     * 注意：更新好友相关信息，如加备注名，必须是好友才可以
     *
     * @param yunXinFriendUpdate {@link YunXinFriendUpdate}
     * @return success return true
     */
    public boolean friendUpdate(YunXinFriendUpdate yunXinFriendUpdate) {
        JSONObject jsonObject = super.request(YunXinActionEnum.FRIEND_UPDATE, yunXinFriendUpdate);
        return jsonObject != null;
    }

    /**
     * 删除好友
     * <p>
     * 注意：删除好友关系
     *
     * @param yunXinFriendDelete {@link YunXinFriendDelete}
     * @return success return true
     */
    public boolean friendDelete(YunXinFriendDelete yunXinFriendDelete) {
        JSONObject jsonObject = super.request(YunXinActionEnum.FRIEND_DELETE, yunXinFriendDelete);
        return jsonObject != null;
    }


    /**
     * 获取好友关系
     * <p>
     * 查询某时间点起到现在有更新的双向好友
     *
     * @param yunXinFriendGet {@link YunXinFriendGet}
     * @return success return true
     */
    public List<YunXinFriend> friendGet(YunXinFriendGet yunXinFriendGet) {
        JSONObject jsonObject = super.request(YunXinActionEnum.FRIEND_GET, yunXinFriendGet);
        String friends = jsonObject.getString("friends");
        if (friends == null || friends.length() == 0) {
            return Collections.emptyList();
        }

        return JSON.parseArray(friends, YunXinFriend.class);
    }

    /**
     * 设置黑名单/静音
     * <p>
     * 拉黑/取消拉黑；设置静音/取消静音
     *
     * @param yunXinUserSetSpecialRelation {@link YunXinUserSetSpecialRelation}
     * @return success return true
     */
    public boolean userSetSpecialRelation(YunXinUserSetSpecialRelation yunXinUserSetSpecialRelation) {
        JSONObject jsonObject = super.request(YunXinActionEnum.USER_SET_SPECIAL_RELATION, yunXinUserSetSpecialRelation);
        return jsonObject != null;
    }

    /**
     * 查看指定用户的黑名单和静音列表
     *
     * @param accid IM accid
     * @return {@link YunXinUserListBlackAndMuteList}
     */
    public YunXinUserListBlackAndMuteList userListBlackAndMuteList(String accid) {
        YunXinUserListBlackAndMuteList yunXinUserListBlackAndMuteList = new YunXinUserListBlackAndMuteList();
        JSONObject jsonObject = super.request(YunXinActionEnum.USER_LIST_BLACK_AND_MUTE_LIST, "accid", accid);
        String muteList = jsonObject.getString("mutelist");
        if (muteList != null && muteList.length() != 0) {
            yunXinUserListBlackAndMuteList.setMutelist(JSON.parseArray(muteList, String.class));
        }

        String blackList = jsonObject.getString("blacklist");
        if (blackList != null && blackList.length() != 0) {
            yunXinUserListBlackAndMuteList.setBlacklist(JSON.parseArray(blackList, String.class));
        }

        return yunXinUserListBlackAndMuteList;
    }

}
