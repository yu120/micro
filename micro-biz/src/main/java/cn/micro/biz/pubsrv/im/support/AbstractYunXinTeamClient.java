package cn.micro.biz.pubsrv.im.support;

import cn.micro.biz.pubsrv.im.YunXinActionEnum;
import cn.micro.biz.pubsrv.im.model.team.*;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import java.util.Collections;
import java.util.List;

/**
 * YunXin Team
 *
 * @author lry
 */
public abstract class AbstractYunXinTeamClient extends AbstractYunXinMsgClient {

    /**
     * 创建群
     * <p>
     * 接口描述:
     * 1.创建高级群，以邀请的方式发送给用户；
     * 2.custom 字段是给第三方的扩展字段，第三方可以基于此字段扩展高级群的功能，构建自己需要的群；
     * 3.建群成功会返回tid，需要保存，以便于加人与踢人等后续操作；
     * 4.每个用户可创建的群数量有限制，限制值由 IM 套餐的群组配置决定，可登录管理后台查看。
     *
     * @param yunXinTeamCreate {@link YunXinTeamCreate}
     * @return success return true
     */
    public boolean teamCreate(YunXinTeamCreate yunXinTeamCreate) {
        JSONObject jsonObject = super.request(YunXinActionEnum.TEAM_CREATE, yunXinTeamCreate);
        yunXinTeamCreate.setTid(jsonObject.getString("tid"));
        return yunXinTeamCreate.getTid() != null && yunXinTeamCreate.getTid().length() > 0;
    }

    /**
     * 拉人入群
     * <p>
     * 接口描述:
     * 1.可以批量邀请，邀请时需指定群主；
     * 2.当群成员达到上限时，再邀请某人入群返回失败；
     * 3.当群成员达到上限时，被邀请人“接受邀请"的操作也将返回失败。
     *
     * @param yunXinTeamAdd {@link YunXinTeamAdd}
     * @return success return true
     */
    public boolean teamAdd(YunXinTeamAdd yunXinTeamAdd) {
        JSONObject jsonObject = super.request(YunXinActionEnum.TEAM_ADD, yunXinTeamAdd);
        return jsonObject != null;
    }

    /**
     * 踢人出群
     * <p>
     * 接口描述:
     * 1.高级群踢人出群，需要提供群主accid以及要踢除人的accid。
     *
     * @param yunXinTeamKick {@link YunXinTeamKick}
     * @return success return true
     */
    public boolean teamKick(YunXinTeamKick yunXinTeamKick) {
        JSONObject jsonObject = super.request(YunXinActionEnum.TEAM_KICK, yunXinTeamKick);
        return jsonObject != null;
    }

    /**
     * 解散群
     * <p>
     * 接口描述：
     * 1.删除整个群，会解散该群，需要提供群主accid，谨慎操作！
     *
     * @param yunXinTeamRemove {@link YunXinTeamRemove}
     * @return success return true
     */
    public boolean teamRemove(YunXinTeamRemove yunXinTeamRemove) {
        JSONObject jsonObject = super.request(YunXinActionEnum.TEAM_REMOVE, yunXinTeamRemove);
        return jsonObject != null;
    }

    /**
     * 编辑群资料
     * <p>
     * 接口描述:
     * 1.高级群基本信息修改
     *
     * @param yunXinTeamUpdate {@link YunXinTeamUpdate}
     * @return success return true
     */
    public boolean teamUpdate(YunXinTeamUpdate yunXinTeamUpdate) {
        JSONObject jsonObject = super.request(YunXinActionEnum.TEAM_UPDATE, yunXinTeamUpdate);
        return jsonObject != null;
    }

    /**
     * 群信息与成员列表查询
     * <p>
     * 接口描述:
     * 1.高级群信息与成员列表查询，一次最多查询30个群相关的信息，跟据ope参数来控制是否带上群成员列表；
     * 2.查询群成员会稍微慢一些，所以如果不需要群成员列表可以只查群信息；
     * 3.此接口受频率控制，某个应用一分钟最多查询30次，超过会返回416，并且被屏蔽一段时间；
     * 4.群成员的群列表信息中增加管理员成员admins的返回。
     *
     * @param yunXinTeamQuery {@link YunXinTeamQuery}
     * @return {@link List<YunXinTeamQueryInfo>}
     */
    public List<YunXinTeamQueryInfo> teamQuery(YunXinTeamQuery yunXinTeamQuery) {
        JSONObject jsonObject = super.request(YunXinActionEnum.TEAM_QUERY, yunXinTeamQuery);
        String tinfos = jsonObject.getString("tinfos");
        if (tinfos == null || tinfos.length() == 0) {
            return Collections.emptyList();
        }

        return JSON.parseArray(tinfos, YunXinTeamQueryInfo.class);
    }

    /**
     * 获取群组详细信息
     * <p>
     * 接口描述:
     * 1.查询指定群的详细信息（群信息+成员详细信息）
     *
     * @param tid IM team id
     * @return {@link YunXinTeamQueryDetailInfo}
     */
    public YunXinTeamQueryDetailInfo teamQueryDetail(String tid) {
        JSONObject jsonObject = super.request(YunXinActionEnum.TEAM_QUERY_DETAIL, "tid", tid);
        String tinfo = jsonObject.getString("tinfo");
        if (tinfo == null || tinfo.length() == 0) {
            return null;
        }

        return JSON.parseObject(tinfo, YunXinTeamQueryDetailInfo.class);
    }

    /**
     * 获取群组已读消息的已读详情信息
     * <p>
     * 接口描述:
     * 1.获取群组已读消息的已读详情信息
     *
     * @param yunXinTeamGetMarkReadInfo {@link YunXinTeamGetMarkReadInfo}
     * @return {@link YunXinTeamGetMarkReadInfoData}
     */
    public YunXinTeamGetMarkReadInfoData teamGetMarkReadInfo(YunXinTeamGetMarkReadInfo yunXinTeamGetMarkReadInfo) {
        JSONObject jsonObject = super.request(YunXinActionEnum.TEAM_GET_MARK_READ_INFO, yunXinTeamGetMarkReadInfo);
        String data = jsonObject.getString("data");
        if (data == null || data.length() == 0) {
            return null;
        }

        return JSON.parseObject(data, YunXinTeamGetMarkReadInfoData.class);
    }

    /**
     * 移交群主
     * <p>
     * 接口描述:
     * 1.转换群主身份
     * 2.群主可以选择离开此群，还是留下来成为普通成员
     *
     * @param yunXinTeamChangeOwner {@link YunXinTeamChangeOwner}
     * @return success return true
     */
    public boolean teamChangeOwner(YunXinTeamChangeOwner yunXinTeamChangeOwner) {
        JSONObject jsonObject = super.request(YunXinActionEnum.TEAM_CHANGE_OWNER, yunXinTeamChangeOwner);
        return jsonObject != null;
    }

    /**
     * 任命管理员
     * <p>
     * 接口描述:
     * 1.提升普通成员为群管理员，可以批量，但是一次添加最多不超过10个人。
     *
     * @param yunXinTeamAddManager {@link YunXinTeamAddManager}
     * @return success return true
     */
    public boolean teamAddManager(YunXinTeamAddManager yunXinTeamAddManager) {
        JSONObject jsonObject = super.request(YunXinActionEnum.TEAM_ADD_MANAGER, yunXinTeamAddManager);
        return jsonObject != null;
    }

    /**
     * 移除管理员
     * <p>
     * 接口描述:
     * 1.解除管理员身份，可以批量，但是一次解除最多不超过10个人
     *
     * @param yunXinTeamRemoveManager {@link YunXinTeamRemoveManager}
     * @return success return true
     */
    public boolean teamRemoveManager(YunXinTeamRemoveManager yunXinTeamRemoveManager) {
        JSONObject jsonObject = super.request(YunXinActionEnum.TEAM_REMOVE_MANAGER, yunXinTeamRemoveManager);
        return jsonObject != null;
    }

    /**
     * 修改群昵称
     * <p>
     * 接口描述:
     * 1.修改指定账号在群内的昵称
     *
     * @param yunXinTeamUpdateTeamNick {@link YunXinTeamUpdateTeamNick}
     * @return success return true
     */
    public boolean teamUpdateTeamNick(YunXinTeamUpdateTeamNick yunXinTeamUpdateTeamNick) {
        JSONObject jsonObject = super.request(YunXinActionEnum.TEAM_UPDATE_TEAM_NICK, yunXinTeamUpdateTeamNick);
        return jsonObject != null;
    }

    /**
     * 修改消息提醒开关
     * <p>
     * 接口描述:
     * 1.高级群修改消息提醒开关
     *
     * @param yunXinTeamMuteTeam {@link YunXinTeamMuteTeam}
     * @return success return true
     */
    public boolean teamMuteTeam(YunXinTeamMuteTeam yunXinTeamMuteTeam) {
        JSONObject jsonObject = super.request(YunXinActionEnum.TEAM_MUTE_TEAM, yunXinTeamMuteTeam);
        return jsonObject != null;
    }

    /**
     * 禁言群成员
     * <p>
     * 接口描述:
     * 1.高级群禁言群成员
     *
     * @param yunXinTeamMuteTlist {@link YunXinTeamMuteTlist}
     * @return success return true
     */
    public boolean teamMuteTlist(YunXinTeamMuteTlist yunXinTeamMuteTlist) {
        JSONObject jsonObject = super.request(YunXinActionEnum.TEAM_MUTE_TLIST, yunXinTeamMuteTlist);
        return jsonObject != null;
    }

    /**
     * 主动退群
     * <p>
     * 接口描述:
     * 1.高级群主动退群
     *
     * @param yunXinTeamLeave {@link YunXinTeamLeave}
     * @return success return true
     */
    public boolean teamLeave(YunXinTeamLeave yunXinTeamLeave) {
        JSONObject jsonObject = super.request(YunXinActionEnum.TEAM_LEAVE, yunXinTeamLeave);
        return jsonObject != null;
    }

    /**
     * 将群组整体禁言
     * <p>
     * 接口描述:
     * 1.禁言群组，普通成员不能发送消息，创建者和管理员可以发送消息
     *
     * @param yunXinTeamMuteTlistAll {@link YunXinTeamMuteTlistAll}
     * @return success return true
     */
    public boolean teamMuteTlistAll(YunXinTeamMuteTlistAll yunXinTeamMuteTlistAll) {
        JSONObject jsonObject = super.request(YunXinActionEnum.TEAM_MUTE_TLIST_ALL, yunXinTeamMuteTlistAll);
        return jsonObject != null;
    }

    /**
     * 获取群组禁言列表
     * <p>
     * 接口描述:
     * 1.获取群组禁言的成员列表
     *
     * @param yunXinTeamListTeamMute {@link YunXinTeamListTeamMute}
     * @return success return true
     */
    public boolean teamListTeamMute(YunXinTeamListTeamMute yunXinTeamListTeamMute) {
        JSONObject jsonObject = super.request(YunXinActionEnum.TEAM_LIST_TEAM_MUTE, yunXinTeamListTeamMute);
        return jsonObject != null;
    }

}
