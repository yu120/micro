package cn.micro.biz.pubsrv.dingtalk.message;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * Ding Talk Action Card
 *
 * @author lry
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DingTalkOA implements Serializable {

    @JSONField(name = "body")
    private Body body;
    @JSONField(name = "head")
    private Head head;
    @JSONField(name = "message_url")
    private String messageUrl;
    @JSONField(name = "pc_message_url")
    private String pcMessageUrl;

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

}
