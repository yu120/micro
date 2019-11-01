package cn.micro.biz.pubsrv.dingtalk.message;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Msg Type
 *
 * @author lry
 */
@Getter
@AllArgsConstructor
public enum DingTalkMsgType {

    // ===
    ACTION_CARD("action_card"), FILE("file"), IMAGE("image"), LINK("link"),
    MARKDOWN("markdown"), OA("oa"), TEXT("text"), VOICE("voice");

    private String code;

}
