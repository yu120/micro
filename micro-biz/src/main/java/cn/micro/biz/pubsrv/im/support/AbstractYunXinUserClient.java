package cn.micro.biz.pubsrv.im.support;

import cn.micro.biz.pubsrv.im.AbstractYunXin;
import cn.micro.biz.pubsrv.im.YunXinActionEnum;
import cn.micro.biz.pubsrv.im.model.user.YunXinUpdateUinfo;
import cn.micro.biz.pubsrv.im.model.user.YunXinUserCreate;
import cn.micro.biz.pubsrv.im.model.user.YunXinUserMute;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import java.util.List;

/**
 * 通信用户
 *
 * @author lry
 */
public abstract class AbstractYunXinUserClient extends AbstractYunXin {

    /**
     * 创建网易云通信ID
     * <p>
     * 接口描述:
     * 1.第三方帐号导入到网易云通信平台；
     * 2.注意accid，name长度以及考虑管理token。
     *
     * @param yunXinUserCreate {@link YunXinUserCreate}
     */
    public void userCreate(YunXinUserCreate yunXinUserCreate) {
        JSONObject jsonObject = super.request(YunXinActionEnum.USER_CREATE, yunXinUserCreate);
        JSONObject info = jsonObject.getJSONObject("info");
        yunXinUserCreate.setAccid(info.getString("accid"));
        yunXinUserCreate.setToken(info.getString("token"));
    }

    /**
     * 更新并获取新token
     * <p>
     * 接口描述:
     * 1.webserver更新网易云通信ID的token，同时返回新的token；
     * 2.一般用于网易云通信ID修改密码，找回密码或者第三方有需求获取新的token。
     *
     * @param accid IM accid
     * @return IM token
     */
    public String userRefreshToken(String accid) {
        JSONObject jsonObject = super.request(YunXinActionEnum.USER_REFRESH_TOKEN, "accid", accid);
        return jsonObject.getJSONObject("info").getString("token");
    }

    /**
     * 更新用户名片
     *
     * @param yunXinUpdateUinfo {@link YunXinUpdateUinfo}
     * @return success return true
     */
    public boolean userUpdateUinfo(YunXinUpdateUinfo yunXinUpdateUinfo) {
        JSONObject jsonObject = super.request(YunXinActionEnum.USER_UPDATE_UINFO, yunXinUpdateUinfo);
        return jsonObject != null;
    }

    /**
     * 批量获取用户名片
     *
     * @param accids accid list
     * @return {@link List<YunXinUserCreate>}
     */
    public List<YunXinUserCreate> userGetUinfos(List<String> accids) {
        String accidsJSON = JSON.toJSONString(accids);
        JSONObject jsonObject = super.request(YunXinActionEnum.USER_GET_UINFOS, "accids", accidsJSON);
        return JSON.parseArray(jsonObject.getString("uinfos"), YunXinUserCreate.class);
    }

    /**
     * 账号全局禁言
     *
     * @param yunXinUserMute {@link YunXinUserMute}
     * @return success return true
     */
    public boolean userMute(YunXinUserMute yunXinUserMute) {
        JSONObject jsonObject = super.request(YunXinActionEnum.USER_MUTE, yunXinUserMute);
        return jsonObject != null;
    }

}
