package cn.micro.biz.pubsrv.im;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Yun Xingit config --list Action Type
 *
 * @author lry
 */
@Getter
@AllArgsConstructor
public enum YunXinActionEnum {

    // ====== 网易云通信ID

    USER_CREATE("/user/create.action", "创建网易云通信ID"),
    USER_UPDATE("/user/update.action", "网易云通信ID更新"),
    USER_REFRESH_TOKEN("/user/doRefreshToken.action", "更新并获取新token"),
    USER_BLOCK("/user/block.action", "封禁网易云通信ID"),
    USER_UNBLOCK("/user/unblock.action", "解禁网易云通信ID"),
    // == 用户名片
    USER_UPDATE_UINFO("/user/updateUinfo.action", "更新用户名片"),
    USER_GET_UINFOS("/user/getUinfos.action", "批量获取用户名片"),
    // == 用户设置
    USER_SET_DONNOP("/user/setDonnop.action", "设置桌面端在线时，移动端是否需要推送"),
    USER_MUTE("/user/mute.action", "账号全局禁言"),
    USER_MUTE_AV("/user/muteAv.action", "账号全局禁用音视频"),

    // ====== 用户关系托管

    FRIEND_ADD("/friend/add.action", "加好友"),
    FRIEND_UPDATE("/friend/update.action", "更新好友相关信息"),
    FRIEND_DELETE("/friend/delete.action", "删除好友"),
    FRIEND_GET("/friend/get.action", "获取好友关系"),
    USER_SET_SPECIAL_RELATION("/user/setSpecialRelation.action", "设置黑名单/静音"),
    USER_LIST_BLACK_AND_MUTE_LIST("/user/listBlackAndMuteList.action", "查看指定用户的黑名单和静音列表"),

    // ====== 消息功能

    MSG_SEND_MSG("/msg/sendMsg.action", "发送普通消息"),
    MSG_SEND_BATCH_MSG("/msg/sendBatchMsg.action", "批量发送点对点普通消息"),
    MSG_SEND_ATTACH_MSG("/msg/sendAttachMsg.action", "发送自定义系统通知"),
    MSG_SEND_BATCH_ATTACH_MSG("/msg/sendBatchAttachMsg.action", "批量发送点对点自定义系统通知"),
    MSG_UPLOAD("/msg/upload.action", "文件上传"),
    MSG_FILE_UPLOAD("/msg/fileUpload.action", "文件上传（multipart方式）"),
    JOB_NOS_DEL("/job/nos/del.action", "上传NOS文件清理任务"),
    MSG_RECALL("/msg/recall.action", "消息撤回"),
    MSG_BROADCAST_MSG("/msg/broadcastMsg.action", "发送广播消息"),

    // ====== 群组功能（高级群）

    TEAM_CREATE("/team/create.action", "创建群"),
    TEAM_ADD("/team/add.action", "拉人入群"),
    TEAM_KICK("/team/kick.action", "踢人出群"),
    TEAM_REMOVE("/team/remove.action", "解散群"),
    TEAM_UPDATE("/team/update.action", "编辑群资料"),
    TEAM_QUERY("/team/query.action", "群信息与成员列表查询"),
    TEAM_QUERY_DETAIL("/team/queryDetail.action", "获取群组详细信息"),
    TEAM_GET_MARK_READ_INFO("/team/getMarkReadInfo.action", "获取群组已读消息的已读详情信息"),
    TEAM_CHANGE_OWNER("/team/changeOwner.action", "移交群主"),
    TEAM_ADD_MANAGER("/team/addManager.action", "任命管理员"),
    TEAM_REMOVE_MANAGER("/team/removeManager.action", "移除管理员"),
    TEAM_JOIN_TEAMS("/team/joinTeams.action", "获取某用户所加入的群信息"),
    TEAM_UPDATE_TEAM_NICK("/team/updateTeamNick.action", "修改群昵称"),
    TEAM_MUTE_TEAM("/team/muteTeam.action", "修改消息提醒开关"),
    TEAM_MUTE_TLIST("/team/muteTlist.action", "禁言群成员"),
    TEAM_LEAVE("/team/leave.action", "主动退群"),
    TEAM_MUTE_TLIST_ALL("/team/muteTlistAll.action", "将群组整体禁言"),
    TEAM_LIST_TEAM_MUTE("/team/listTeamMute.action", "获取群组禁言列表"),

    // ====== 聊天室

    CHATROOM_CREATE("/chatroom/create.action", "创建聊天室"),
    CHATROOM_GET("/chatroom/get.action", "查询聊天室信息"),
    CHATROOM_GET_BATCH("/chatroom/getBatch.action", "批量查询聊天室信息"),
    CHATROOM_UPDATE("/chatroom/update.action", "更新聊天室信息"),
    CHATROOM_TOGGLE_CLOSE_STAT("/chatroom/toggleCloseStat.action", "修改聊天室开/关闭状态"),
    CHATROOM_SET_MEMBER_ROLE("/chatroom/setMemberRole.action", "设置聊天室内用户角色"),
    CHATROOM_REQUEST_ADDR("/chatroom/requestAddr.action", "请求聊天室地址"),
    CHATROOM_SEND_MSG("/chatroom/sendMsg.action", "发送聊天室消息"),
    CHATROOM_ADD_ROBOT("/chatroom/addRobot.action", "往聊天室内添加机器人"),
    CHATROOM_REMOVE_ROBOT("/chatroom/removeRobot.action", "从聊天室内删除机器人"),
    CHATROOM_TEMPORARY_MUTE("/chatroom/temporaryMute.action", "设置临时禁言状态"),
    CHATROOM_QUEUE_OFFER("/chatroom/queueOffer.action", "往聊天室有序队列中新加或更新元素"),
    CHATROOM_QUEUE_POLL("/chatroom/queuePoll.action", "从队列中取出元素"),
    CHATROOM_QUEUE_LIST("/chatroom/queueList.action", "排序列出队列中所有元素"),
    CHATROOM_QUEUE_DROP("/chatroom/queueDrop.action", "删除清理整个队列"),
    CHATROOM_QUEUE_INIT("/chatroom/queueInit.action", "初始化队列"),
    CHATROOM_MUTE_ROOM("/chatroom/muteRoom.action", "将聊天室整体禁言"),
    STATS_CHATROOM_TOPN("/stats/chatroom/topn.action", "查询聊天室统计指标TopN"),
    CHATROOM_MEMBERS_BY_PAGE("/chatroom/membersByPage.action", "分页获取成员列表"),
    CHATROOM_QUERY_MEMBERS("/chatroom/queryMembers.action", "批量获取在线成员信息"),
    CHATROOM_UPDATE_MY_ROOM_ROLE("/chatroom/updateMyRoomRole.action", "变更聊天室内的角色信息"),
    CHATROOM_QUEUE_BATCH_UPDATE_ELEMENTS("/chatroom/queueBatchUpdateElements.action", "批量更新聊天室队列元素"),
    CHATROOM_QUERY_USER_ROOM_IDS("/chatroom/queryUserRoomIds.action", "查询用户创建的开启状态聊天室列表"),

    // ====== 历史记录

    HISTORY_QUERY_SESSION_MSG("/history/querySessionMsg.action", "单聊云端历史消息查询"),
    HISTORY_QUERY_TEAM_MSG("/history/queryTeamMsg.action", "群聊云端历史消息查询"),
    HISTORY_QUERY_CHATROOM_MSG("/history/queryChatroomMsg.action", "聊天室云端历史消息查询"),
    CHATROOM_DELETE_HISTORY_MESSAGE("/chatroom/deleteHistoryMessage.action", "删除聊天室云端历史消息"),
    HISTORY_QUERY_USER_EVENTS("/history/queryUserEvents.action", "用户登录登出事件记录查询"),
    HISTORY_QUERY_BROADCAST_MSG("/history/queryBroadcastMsg.action", "批量查询广播消息"),
    HISTORY_QUERY_BROADCAST_MSG_BY_ID("/history/queryBroadcastMsgById.action", "查询单条广播消息"),

    // ====== 在线状态

    EVENT_SUBSCRIBE_ADD("/event/subscribe/add.action", "订阅在线状态事件"),
    EVENT_SUBSCRIBE_DELETE("/event/subscribe/delete.action", "取消在线状态事件订阅"),
    EVENT_SUBSCRIBE_BATCH_DEL("/event/subscribe/batchdel.action", "取消全部在线状态事件订阅"),
    EVENT_SUBSCRIBE_QUERY("/event/subscribe/query.action", "查询在线状态事件订阅关系");

    private final String value;
    private final String title;

}