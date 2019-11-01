package cn.micro.biz.pubsrv.dingtalk.request;

import cn.micro.biz.pubsrv.dingtalk.AbstractOpenDingTalk;
import com.alibaba.fastjson.annotation.JSONField;
import lombok.*;

import java.io.Serializable;
import java.util.List;

/**
 * 异步发送工作通知消息请求
 *
 * @author lry
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class DingTalkMessageAsyncRequest extends DingTalkRequest {

    @JSONField(name = "agent_id")
    private Long agentId;
    @JSONField(name = "dept_id_list")
    private String deptIdList;
    private Msg msg;
    @JSONField(name = "to_all_user")
    private Boolean toAllUser;
    @JSONField(name = "userid_list")
    private String useridList;

    public DingTalkMessageAsyncRequest() {
        super(AbstractOpenDingTalk.HttpMethod.POST,
                "https://oapi.dingtalk.com/topapi/message/corpconversation/asyncsend_v2?access_token=%s");
    }

    public void setDeptIdList(List<String> deptIdList) {
        this.deptIdList = String.join(",", deptIdList.toArray(new String[0]));
    }

    public void setUserIdList(List<String> userIdList) {
        this.useridList = String.join(",", userIdList.toArray(new String[0]));
    }

    @Getter
    @AllArgsConstructor
    public enum MsgType {
        // ===
        ACTION_CARD("action_card"), FILE("file"), IMAGE("image"), LINK("link"),
        MARKDOWN("markdown"), OA("oa"), TEXT("text"), VOICE("voice");
        private String code;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Msg implements Serializable {
        @JSONField(name = "msgtype")
        private String msgtype;

        @JSONField(name = "action_card")
        private ActionCard actionCard;
        @JSONField(name = "file")
        private File file;
        @JSONField(name = "image")
        private Image image;
        @JSONField(name = "link")
        private Link link;
        @JSONField(name = "markdown")
        private Markdown markdown;
        @JSONField(name = "oa")
        private OA oa;
        @JSONField(name = "text")
        private Text text;
        @JSONField(name = "voice")
        private Voice voice;

        public void setMsgType(MsgType msgType) {
            this.msgtype = msgType.getCode();
        }
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ActionCard implements Serializable {
        @JSONField(name = "btn_json_list")
        private List<BtnJsonList> btnJsonList;
        @JSONField(name = "btn_orientation")
        private String btnOrientation;
        @JSONField(name = "markdown")
        private String markdown;
        @JSONField(name = "single_title")
        private String singleTitle;
        @JSONField(name = "single_url")
        private String singleUrl;
        @JSONField(name = "title")
        private String title;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class BtnJsonList implements Serializable {
        @JSONField(name = "action_url")
        private String actionUrl;
        @JSONField(name = "title")
        private String title;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Markdown implements Serializable {
        @JSONField(name = "text")
        private String text;
        @JSONField(name = "title")
        private String title;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class OA implements Serializable {
        @JSONField(name = "body")
        private Body body;
        @JSONField(name = "head")
        private Head head;
        @JSONField(name = "message_url")
        private String messageUrl;
        @JSONField(name = "pc_message_url")
        private String pcMessageUrl;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Head implements Serializable {
        @JSONField(name = "bgcolor")
        private String bgcolor;
        @JSONField(name = "text")
        private String text;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Body implements Serializable {
        @JSONField(name = "author")
        private String author;
        @JSONField(name = "content")
        private String content;
        @JSONField(name = "file_count")
        private String fileCount;
        @JSONField(name = "form")
        private List<Form> form;
        @JSONField(name = "image")
        private String image;
        @JSONField(name = "rich")
        private Rich rich;
        @JSONField(name = "title")
        private String title;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Form implements Serializable {
        @JSONField(name = "key")
        private String key;
        @JSONField(name = "value")
        private String value;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Rich implements Serializable {
        @JSONField(name = "num")
        private String num;
        @JSONField(name = "unit")
        private String unit;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Voice implements Serializable {
        @JSONField(name = "duration")
        private String duration;
        @JSONField(name = "media_id")
        private String mediaId;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class File implements Serializable {
        @JSONField(name = "media_id")
        private String mediaId;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Link implements Serializable {
        @JSONField(name = "messageUrl")
        private String messageUrl;
        @JSONField(name = "picUrl")
        private String picUrl;
        @JSONField(name = "text")
        private String text;
        @JSONField(name = "title")
        private String title;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Image implements Serializable {
        @JSONField(name = "media_id")
        private String mediaId;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Text implements Serializable {
        @JSONField(name = "content")
        private String content;
    }

}
