package cn.micro.biz.pubsrv.dingtalk.message;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * Ding Talk Action Card
 *
 * @author lry
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DingTalkLink implements Serializable {

    @JSONField(name = "messageUrl")
    private String messageUrl;
    @JSONField(name = "picUrl")
    private String picUrl;
    @JSONField(name = "text")
    private String text;
    @JSONField(name = "title")
    private String title;

}
