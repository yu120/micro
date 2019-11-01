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
public class DingTalkText implements Serializable {

    @JSONField(name = "content")
    private String content;

}
